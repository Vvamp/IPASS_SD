package org.vvamp.ingenscheveer.models.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupedScheduleDay {
    private final long date;
    private List<ScheduleTask> tasks;

    public GroupedScheduleDay(long date, ArrayList<ScheduleTask> tasks) {
        this.date = date;
        this.tasks = tasks;
    }

    @JsonProperty("Day")
    public long getDate() {
        return date;
    }

    @JsonProperty("Tasks")
    public List<ScheduleTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<ScheduleTask> tasks) {
        this.tasks = tasks;
    }

    public void addTask(ScheduleTask task){
        this.tasks.add(task);
    }
}
