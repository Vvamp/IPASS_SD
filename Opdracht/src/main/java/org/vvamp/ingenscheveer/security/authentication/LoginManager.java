package org.vvamp.ingenscheveer.security.authentication;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

public class LoginManager {
    private static Map<String, User> users;
    private static List<String> validatedTokens;
    private static Key key;
    private final Clock clock;

    public LoginManager() {
        this(Clock.systemDefaultZone());
        populate();
    }

    public LoginManager(Clock clock) {
        if (users == null) {
            users = new HashMap<>();
        }
        if (validatedTokens == null) {
            validatedTokens = new ArrayList<>();
        }
        if (key == null) {
            key = MacProvider.generateKey();
        }
        this.clock = clock;
    }

    public void populate() {
        users = new HashMap<>();
        users.put("Vvamp", new User("Vvamp", "admin", "user"));

    }


    public void addUser(User user) {
        if (users.containsKey(user.getName())) {
            throw new IllegalArgumentException("User with name " + user.getName() + " already exists");
        }
        users.put(user.getName(), user);

    }

    public String validateLogin(String username, String password) {
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("User '" + username + "' not found");
        }

        if (!users.get(username).matchCredentials(username, password)) {
            throw new IllegalArgumentException("Wrong password");
        }
        String role = users.get(username).getRole();
        return role;
    }

    public String createToken(String username, String role) {
        Instant now = clock.instant();
        Instant expiration = now.plusSeconds(30 * 60);

        return Jwts.builder().setSubject(username).setExpiration(Date.from(expiration)).claim("role", role).setIssuedAt(Date.from(now)).signWith(SignatureAlgorithm.HS256, key).compact();
    }

    public void validateToken(String token) {
        validatedTokens.add(token);
        System.out.println("Added token to valid list. Items:");
        for(String t : validatedTokens) {
            System.out.println("> " + t);
        }
    }

    public ValidationResult checkTokenValidity(String token) {
        return checkTokenValidity(token, null);
    }

    public ValidationResult checkTokenValidity(String token, String username) {
        if (!validatedTokens.contains(token)) {
            System.out.println("Token " + token + " not found in items:");
            for(String t : validatedTokens) {
                System.out.println("> " + t);
            }
            return new ValidationResult(ValidationStatus.INVALID, "The token was not in the authorisation list.");
        }

        JwtParser parser = Jwts.parser().setSigningKey(key);
        Claims claims = parser.parseClaimsJws(token).getBody();
        User user = users.get(claims.getSubject());
        if (username != null) {
            if (!claims.getSubject().equals(username))
                return new ValidationResult(ValidationStatus.INVALID, "The token was not found in the user's token list.");
        }

        Instant now = clock.instant();

        if (claims.getExpiration().before(Date.from(now))) {
            return new ValidationResult(ValidationStatus.EXPIRED, String.format("The token has expired. ( %s < %s )", claims.getExpiration().toString(), Date.from(now).getTime()));
        }

        return new ValidationResult(ValidationStatus.VALID, "No issues with the token were found.", user);
    }

    public void invalidateToken(String token) {
        try {
            validatedTokens.remove(token);
        } catch (Exception e) {
        }
    }

    public List<User> getUsers() {
        return users.values().stream().toList();
    }
}
