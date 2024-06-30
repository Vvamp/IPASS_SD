package org.vvamp.ingenscheveer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.security.authentication.LoginManager;
import org.vvamp.ingenscheveer.security.authentication.User;
import org.vvamp.ingenscheveer.security.authentication.ValidationResult;
import org.vvamp.ingenscheveer.security.authentication.ValidationStatus;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginManagerTest {
    LoginManager loginManager;
    String testName;
    String testPassword;
    String testRole;
    User testUser;

    private void resetLoginManager() throws NoSuchFieldException, IllegalAccessException {
        Field usersField = LoginManager.class.getDeclaredField("users");
        usersField.setAccessible(true);
        usersField.set(null, new HashMap<String, User>());

        DatabaseStorageController.getDatabaseTokenController().removeAllTokens();
    }

    @BeforeEach
    public void init() {
        try {
            resetLoginManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        loginManager = new LoginManager();

        testName = "testUser";
        testPassword = "testPassword";
        testRole = "testRole";
        testUser = new User(testName, testPassword, testRole);
    }

    @Test
    public void testUserValidation() {
        loginManager.addUser(testUser);

        assertEquals(loginManager.validateLogin(testName, testPassword), testRole);
    }

    @Test
    public void testValidateIncorrectPassword() {
        loginManager.addUser(testUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginManager.validateLogin(testName, testPassword + "-");
        });
        assertEquals(exception.getMessage(), "Wrong password");
    }

    @Test
    public void testValidateUnknownUser() {
        // Now let's say we forget to add the user to the loginManager, woops totally not on purpose


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginManager.validateLogin(testName, testPassword + "-");
        });
        assertEquals(exception.getMessage(), "User '" + testName + "' not found");
    }


    @Test
    public void testTokenValidation_Valid() {
        loginManager.addUser(testUser);

        String token = loginManager.createToken(testName, testRole);
        loginManager.validateToken(token);

        ValidationResult result = loginManager.checkTokenValidity(token, testName);
        assertEquals(result.getStatus(), ValidationStatus.VALID);
    }

    @Test
    public void testTokenValidation_Invalid_Invalidated() {
        loginManager.addUser(testUser);

        String token = loginManager.createToken(testName, testRole);
        loginManager.validateToken(token);
        loginManager.invalidateToken(token);

        ValidationResult result = loginManager.checkTokenValidity(token, testName);
        assertEquals(result.getStatus(), ValidationStatus.INVALID);
        assertEquals(result.getDetails(), "The token was not in the authorisation list.");
    }

    @Test
    public void testTokenValidation_Invalid_WrongUser() {
        loginManager.addUser(testUser);

        String token = loginManager.createToken(testName, testRole);
        loginManager.validateToken(token);

        ValidationResult result = loginManager.checkTokenValidity(token, "JohnDoe");
        assertEquals(result.getStatus(), ValidationStatus.INVALID);
        assertEquals(result.getDetails(), "The token was not found in the user's token list.");
    }


    @Test
    public void testTokenValidation_Expired() {

        loginManager.addUser(testUser);

        String token = loginManager.createToken(testName, testRole);
        loginManager.validateToken(token);

        ValidationResult result = loginManager.checkTokenValidity(token, testName);
        assertEquals(result.getStatus(), ValidationStatus.VALID, "Token should be valid right after adding.");

        loginManager = new LoginManager(Clock.offset(Clock.systemDefaultZone(), Duration.ofMinutes(30)));

        result = loginManager.checkTokenValidity(token, testName);
        assertEquals(result.getStatus(), ValidationStatus.EXPIRED, "Token should be expired 30 minutes after adding.");

    }

}
