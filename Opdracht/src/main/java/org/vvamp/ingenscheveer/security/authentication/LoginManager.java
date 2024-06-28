package org.vvamp.ingenscheveer.security.authentication;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.vvamp.ingenscheveer.models.schedule.Schedule;
import org.vvamp.ingenscheveer.models.schedule.ScheduleTask;
import org.vvamp.ingenscheveer.models.schedule.TaskType;

import java.security.Key;
import java.time.*;
import java.util.*;

public class LoginManager {
    private static Map<String, User> users;
    private static List<String> validatedTokens;
    private static Key key;
    private final Clock clock;

    public LoginManager() {
        this(Clock.systemDefaultZone());
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
        System.out.println("Populating login manager");

        users.put("Vincent", new User("Vincent", "admin", "skipper"));
        User a = User.getUserByName("Vincent");
        a.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 1, 6, 0, 0), LocalDateTime.of(2024, 6, 1, 14, 30, 0), "Hello World", a, TaskType.Dienst));
        a.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 2, 6, 0, 0), LocalDateTime.of(2024, 6, 2, 14, 30, 0), "Hello World", a, TaskType.Dienst));
        a.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 3, 6, 0, 0), LocalDateTime.of(2024, 6, 2, 14, 30, 0), "Hello World", a, TaskType.Dienst));
        a.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 4, 6, 0, 0), LocalDateTime.of(2024, 6, 2, 14, 30, 0), "Hello World", a, TaskType.Dienst));
        a.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 5, 6, 0, 0), LocalDateTime.of(2024, 6, 2, 14, 30, 0), "Hello World", a, TaskType.Dienst));
        a.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 6, 6, 0, 0), LocalDateTime.of(2024, 6, 2, 14, 30, 0), "Hello World", a, TaskType.Dienst));

        System.out.println("B");
        users.put("Stephan", new User("Stephan", "schipper", "skipper"));
        User b = User.getUserByName("Stephan");
        b.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 1, 14, 30, 0), LocalDateTime.of(2024, 6, 1, 23, 0, 0), "Hello World", b, TaskType.Dienst));
        b.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 2, 14, 30, 0), LocalDateTime.of(2024, 6, 1, 23, 0, 0), "Hello World", b, TaskType.Dienst));
        b.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 3, 14, 30, 0), LocalDateTime.of(2024, 6, 1, 23, 0, 0), "Hello World", b, TaskType.Dienst));
        b.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 4, 14, 30, 0), LocalDateTime.of(2024, 6, 1, 23, 0, 0), "Hello World", b, TaskType.Dienst));
        b.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 5, 14, 30, 0), LocalDateTime.of(2024, 6, 1, 23, 0, 0), "Hello World", b, TaskType.Dienst));
        b.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 6, 14, 30, 0), LocalDateTime.of(2024, 6, 1, 23, 0, 0), "Hello World", b, TaskType.Dienst));

        users.put("Vincentvl", new User("Vincentvl", "schipper", "skipper"));
        User c = User.getUserByName("Vincentvl");
        c.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 2, 16, 0, 0), LocalDateTime.of(2024, 6, 1, 18, 30, 0), "Hello World", c, TaskType.Kniphulp));
        c.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 4, 16, 0, 0), LocalDateTime.of(2024, 6, 1, 18, 30, 0), "Hello World", c, TaskType.Kniphulp));
        c.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 5, 16, 0, 0), LocalDateTime.of(2024, 6, 1, 18, 30, 0), "Hello World", c, TaskType.Kniphulp));
        c.getSchedule().scheduleTask(new ScheduleTask(LocalDateTime.of(2024, 6, 6, 16, 0, 0), LocalDateTime.of(2024, 6, 1, 18, 30, 0), "Hello World", c, TaskType.Kniphulp));

        users.put("Baas", new User("Baas", "baas", "boss"));


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

    public String getRole(String token){
        if(checkTokenValidity(token).getStatus() != ValidationStatus.VALID)
            return "";

        JwtParser parser = Jwts.parser().setSigningKey(key);
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
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
