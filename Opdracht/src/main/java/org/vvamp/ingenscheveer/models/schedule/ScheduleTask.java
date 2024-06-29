package org.vvamp.ingenscheveer.models.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.security.authentication.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class ScheduleTask {
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private String uuid;



    @JsonIgnore
    private User user;
    private TaskType type;

    public ScheduleTask(LocalDateTime start, LocalDateTime end, String description, User user, TaskType type) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.user = user;
        this.type = type;
        this.uuid = java.util.UUID.randomUUID().toString();
    }

    @JsonIgnore
    public LocalDateTime getStart() {
        return start;
    }

    @JsonProperty("Begin")
    public long getStartEpochMS(){
        return start.toEpochSecond(ZoneOffset.UTC)*1000;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    @JsonIgnore
    public LocalDateTime getEnd() {
        return end;
    }

    @JsonProperty("End")
    public long getEndEpochMS(){
        return end.toEpochSecond(ZoneOffset.UTC)*1000;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @JsonProperty("Type")
    public TaskType getType() {
        return type;
    }

    public User getUser() {
        return user;
    }
    public void setType(TaskType type) {
        this.type = type;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @JsonProperty("Username")
    public String getUsername(){
        return user.getName();
    }

    @JsonProperty("UUID")
    public String getUuid() {
        return uuid;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
