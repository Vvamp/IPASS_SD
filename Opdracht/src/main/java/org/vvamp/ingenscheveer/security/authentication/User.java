package org.vvamp.ingenscheveer.security.authentication;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.models.schedule.Schedule;
import org.vvamp.ingenscheveer.models.schedule.ScheduleTask;
import org.vvamp.ingenscheveer.models.schedule.TaskType;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User implements Principal {
    @JsonIgnore
    private static final List<User> users = new ArrayList<>();

    @JsonProperty
    private final String name;
    @JsonProperty
    private final String role;
    @JsonProperty
    private final String password;
    @JsonProperty
    private final byte[] salt;
    private Schedule schedule;


    public User(String name, String password, String role) {
        this.name = name;
        this.role = role;
        this.salt = Hasher.getSalt();
        this.password = Hasher.hashPassword(password.toCharArray(), this.salt, 10000, 256);
        addUser(this);
        schedule = new Schedule();

    }

    public User(String name, String password) {
        this(name, password, "user");
    }

    public static boolean addUser(User user) {
        if (users.contains(user)) return false;
        users.add(user);
        return true;
    }

    public static User getUserByName(String user) {
        return users.stream().filter(u -> u.getName().equals(user)).findFirst().orElse(null);
    }

    public boolean matchCredentials(String username, String password) {
        String hashedPassword = Hasher.hashPassword(password.toCharArray(), this.salt, 10000, 256);
        return this.name.equals(username) && hashedPassword.equals(this.password);
    }

    @JsonIgnore
    public Schedule getSchedule(){
        return schedule;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User myUser = (User) o;
        return Objects.equals(name, myUser.name);
    }

    public String getRole() {
        return role;
    }

    public static List<User> getAll() {
        return users;
    }

    @Override
    public String getName() {
        return name;
    }
}