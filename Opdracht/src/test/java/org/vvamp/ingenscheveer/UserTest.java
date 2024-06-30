package org.vvamp.ingenscheveer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.models.schedule.ScheduleTask;
import org.vvamp.ingenscheveer.models.schedule.TaskType;
import org.vvamp.ingenscheveer.security.authentication.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @BeforeEach
    public void init(){
        DatabaseStorageController.setUseTest(true);
        DatabaseStorageController.getDatabaseUserController().deleteAllUsers();

    }

    @Test
    public void test_User_Added(){
        User randomuser = new User("testuser", "testpassword", "testrole");
        int id= DatabaseStorageController.getDatabaseUserController().findUserId(randomuser.getName());
        assertNotNull(DatabaseStorageController.getDatabaseUserController().findUserById(id), "User should be added when using regular constructor");
    }

    @Test
    public void test_User_NotAdded(){
        assertEquals(0, DatabaseStorageController.getDatabaseUserController().getAllUsers().size(), "No users should be present when not added");
    }
    @Test
    public void test_User_NotAdded_ExistingUserConstructor(){
        User randomUser = new User("A", "B", "C", new byte[0], -1);
        assertEquals(0, DatabaseStorageController.getDatabaseUserController().getAllUsers().size(), "No users should be added to the database when using the database constructor");
    }

    @Test
    public void test_User_Removed(){
        User randomuser = new User("testuser", "testpassword", "testrole");
        int id= DatabaseStorageController.getDatabaseUserController().findUserId(randomuser.getName());
        DatabaseStorageController.getDatabaseUserController().deleteUserById(id);

        assertNull(DatabaseStorageController.getDatabaseUserController().findUserById(id), "User should be removed when deleting");
    }


}
