package org.vvamp.ingenscheveer.models.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.security.authentication.User;

import java.util.Date;

public class ScheduleTask {
    private Date start;
    private Date end;
    private String description;

    @JsonIgnore
    private User user;
    private TaskType type;

    public ScheduleTask(Date start, Date end, String description, User user, TaskType type) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.user = user;
        this.type = type;
    }


    @JsonProperty("Begin")
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @JsonProperty("End")
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
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


    public void setUser(User user) {
        this.user = user;
    }
}
