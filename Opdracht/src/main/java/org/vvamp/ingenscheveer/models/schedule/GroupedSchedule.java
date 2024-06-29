package org.vvamp.ingenscheveer.models.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.security.authentication.User;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Stream;

public class GroupedSchedule{

    @JsonProperty("Days")
    private ArrayList<GroupedScheduleDay> groupedScheduleDays;

    public static List<GroupedScheduleDay> getGlobalScheduleGrouped(int weeknr){
        WeekFields weekFields = WeekFields.of(Locale.forLanguageTag("nl-NL"));
        LocalDate firstDayOfWeek = LocalDate.ofYearDay(LocalDateTime.now().getYear(), 1)
                .with(weekFields.weekOfYear(), weeknr)
                .with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));


        LocalDate lastDayOfWeek = firstDayOfWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        firstDayOfWeek = firstDayOfWeek.minusDays(1);
        lastDayOfWeek = lastDayOfWeek.minusDays(1);

        List<GroupedScheduleDay> allItems = getGlobalScheduleGrouped();
        List<GroupedScheduleDay> filtered = new ArrayList<>();
        for (GroupedScheduleDay day : allItems) {
            LocalDate date = LocalDateTime.ofEpochSecond(day.getDate(), 0, ZoneOffset.UTC).toLocalDate();
            if((date.isAfter(firstDayOfWeek) && date.isBefore(lastDayOfWeek)) || date.isEqual(firstDayOfWeek) || date.isEqual(lastDayOfWeek)){
                filtered.add(day);
            }
        }
        return filtered;

    }

    public static List<GroupedScheduleDay> getGlobalScheduleGroupedForUser(int weeknr, User user){
        WeekFields weekFields = WeekFields.of(Locale.forLanguageTag("nl-NL"));
        LocalDate firstDayOfWeek = LocalDate.ofYearDay(LocalDateTime.now().getYear(), 1)
                .with(weekFields.weekOfYear(), weeknr)
                .with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));


        LocalDate lastDayOfWeek = firstDayOfWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        firstDayOfWeek = firstDayOfWeek.minusDays(1);
        lastDayOfWeek = lastDayOfWeek.minusDays(1);

        List<GroupedScheduleDay> allItems = getGlobalScheduleGrouped(user);
        List<GroupedScheduleDay> filtered = new ArrayList<>();
        for (GroupedScheduleDay day : allItems) {
            LocalDate date = LocalDateTime.ofEpochSecond(day.getDate(), 0, ZoneOffset.UTC).toLocalDate();
            if((date.isAfter(firstDayOfWeek) && date.isBefore(lastDayOfWeek)) || date.isEqual(firstDayOfWeek) || date.isEqual(lastDayOfWeek)){
                filtered.add(day);
            }
        }
        return filtered;

    }

    public static List<GroupedScheduleDay> getGlobalScheduleGrouped(User user) {
        Schedule schedule = user.getSchedule();
        Schedule outSchedule = new Schedule();
        List<ScheduleTask> tasks = schedule.getTasks();
        for (ScheduleTask t : tasks) {
            outSchedule.scheduleTask(t);

        }


        List<GroupedScheduleDay> groupedScheduleDays = new ArrayList<>();
        for(ScheduleTask task : outSchedule.getTasks()) {
            long date = task.getStart().toLocalDate().toEpochSecond(LocalTime.of(0,0,0), ZoneOffset.UTC);
            GroupedScheduleDay day = groupedScheduleDays.stream().filter(gsd -> gsd.getDate() == date).findFirst().orElse(null);
            if(day == null) {
                GroupedScheduleDay gsd = new GroupedScheduleDay(date, new ArrayList<>());
                gsd.addTask(task);
                groupedScheduleDays.add(gsd);
            }else{
                day.addTask(task);
            }
        }

        return groupedScheduleDays;
    }

    public static List<GroupedScheduleDay> getGlobalScheduleGrouped(){
        List<User> users = User.getAll();
        List<Schedule> schedules = users.stream().flatMap(u -> Stream.of(u.getSchedule())).toList();
        Schedule outSchedule = new Schedule();
        for (Schedule s : schedules) {
            List<ScheduleTask> tasks = s.getTasks();
            for (ScheduleTask t : tasks) {
                outSchedule.scheduleTask(t);
            }
        }


        List<GroupedScheduleDay> groupedScheduleDays = new ArrayList<>();
        for(ScheduleTask task : outSchedule.getTasks()) {
            long date = task.getStart().toLocalDate().toEpochSecond(LocalTime.of(0,0,0), ZoneOffset.UTC);
            GroupedScheduleDay day = groupedScheduleDays.stream().filter(gsd -> gsd.getDate() == date).findFirst().orElse(null);
            if(day == null) {
                GroupedScheduleDay gsd = new GroupedScheduleDay(date, new ArrayList<>());
                gsd.addTask(task);
                groupedScheduleDays.add(gsd);
            }else{
                day.addTask(task);
            }
        }

        return groupedScheduleDays;
    }

}
