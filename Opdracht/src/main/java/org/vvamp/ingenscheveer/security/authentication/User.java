package org.vvamp.ingenscheveer.security.authentication;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.models.schedule.Schedule;
import org.vvamp.ingenscheveer.models.schedule.ScheduleTask;
import org.vvamp.ingenscheveer.models.schedule.TaskType;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User implements Principal {
//    @JsonIgnore
//    private static final List<User> users = new ArrayList<>();
    private int id;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String role;
    @JsonProperty
    private final String password;
    @JsonProperty
    private final byte[] salt;
    private Schedule schedule = null;

    public void loadSchedule(){
        if(schedule != null){
            return;
        }
        schedule = new Schedule();
        List<ScheduleTask> tasks = DatabaseStorageController.getDatabaseScheduleController().getTaskForUser(id);
        for(ScheduleTask task : tasks){
            schedule.scheduleTask(task);
        }
    }

    public User(String name, String password, String role, byte[] salt, int id) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.salt = salt;
        this.id = id;

    }

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

    private boolean addUser(User user) {
//        if (users.contains(user)) return false;
//        users.add(user);
        id = DatabaseStorageController.getDatabaseUserController().writeUser(user);
        return true;
    }

    public static User getUserByName(String user) {
//        return users.stream().filter(u -> u.getName().equals(user)).findFirst().orElse(null);
        return DatabaseStorageController.getDatabaseUserController().getAllUsers().stream().filter(u -> u.getName().equals(user)).findFirst().orElse(null);
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
        return DatabaseStorageController.getDatabaseUserController().getAllUsers();
    }

    @Override
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getSalt() {
        return salt;
    }
}