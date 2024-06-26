package org.vvamp.ingenscheveer.security.authentication;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Principal;
import java.util.ArrayList;
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

    private final byte[] salt;


    public User(String name, String password, String role) {
        this.name = name;
        this.role = role;
        this.salt = Hasher.getSalt();
        this.password = Hasher.hashPassword(password.toCharArray(), this.salt, 10000, 256);
        addUser(this);
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