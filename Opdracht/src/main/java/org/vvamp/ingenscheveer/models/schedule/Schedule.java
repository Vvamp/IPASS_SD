package org.vvamp.ingenscheveer.models.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.security.authentication.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class Schedule {
    private List<ScheduleTask> tasks;

    public Schedule(){
        tasks = new ArrayList<>();

    }
    public void scheduleTask(ScheduleTask scheduleDay){
        tasks.add(scheduleDay);
    }


    @JsonProperty("Tasks")
    public List<ScheduleTask> getTasks(){
        return tasks;
    }

    public ScheduleTask getTaskById(int uuid) {
        for (ScheduleTask task : tasks) {
            if (task.getUuid() == uuid) {
                System.out.println("Matched for " + uuid);
                return task;
            }else{
                System.out.println("No match " + uuid + " -- " + task.getUuid());
            }
        }
        return null;
//        return tasks.stream().filter(task -> task.getUuid().equals(uuid)).findFirst().orElse(null);
    }



    public void setTasks(List<ScheduleTask> tasks){
        this.tasks = tasks;
    }

    public boolean removeTask(int taskUuid){
        ScheduleTask foundTask = tasks.stream().filter(t -> t.getUuid() == taskUuid).findFirst().orElse(null);
        if(foundTask != null){
            tasks.remove(foundTask);
        }

        return foundTask != null;
    }

    @JsonIgnore
    public static Schedule getGlobalSchedule(){
        List<User> users = User.getAll();
        List<Schedule> schedules = users.stream().flatMap(u -> Stream.of(u.getSchedule())).toList();
        Schedule outSchedule = new Schedule();
        for (Schedule s : schedules) {
            List<ScheduleTask> tasks = s.getTasks();
            for (ScheduleTask t : tasks) {
                outSchedule.scheduleTask(t);
            }
        }
        return outSchedule;
    }



}
