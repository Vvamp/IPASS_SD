package org.vvamp.ingenscheveer.models;

import org.vvamp.ingenscheveer.security.authentication.User;

import java.util.Date;

public class ScheduleTask {
    private Date start;
    private Date end;
    private String description;
    private User user;
    private TaskType type;
    public ScheduleTask(Date start, Date end, String description, User user, TaskType type) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.user = user;
        this.type = type;
    }

    public Date getStart() {
        return start;
    }

    public TaskType getType() {
        return type;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setType(TaskType type) {
        this.type = type;
    }
}
