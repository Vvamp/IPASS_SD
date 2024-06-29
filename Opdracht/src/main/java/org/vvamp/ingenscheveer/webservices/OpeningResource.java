package org.vvamp.ingenscheveer.webservices;

import org.vvamp.ingenscheveer.models.api.OpeningTimeResult;
import org.vvamp.ingenscheveer.models.api.OperatingSchedule;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Path("operatingtimes")
public class OpeningResource {

    private LocalDateTime getOpeningTimeForDay(LocalDateTime date) {
        OperatingSchedule schedule = new OperatingSchedule();
        int day = date.getDayOfWeek().getValue()-1;
        LocalTime openingTime = schedule.getDays().get(day).openingTime;
        LocalDateTime openingToday = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), openingTime.getHour(), openingTime.getMinute(), openingTime.getSecond());
        return openingToday;
    }

    private LocalDateTime getClosingTimeForDay(LocalDateTime date) {
        OperatingSchedule schedule = new OperatingSchedule();
        int day = date.getDayOfWeek().getValue()-1;
        LocalTime closingTime = schedule.getDays().get(day).closingTime;
        LocalDateTime closingToday = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), closingTime.getHour(), closingTime.getMinute(), closingTime.getSecond());
        if(date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            closingToday = closingToday.plusDays(1); // saturday ends at midnight
        }
        return closingToday;
    }

    @GET
    @Path("now")
    @Produces(MediaType.APPLICATION_JSON)
    public Response now() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime openingToday = getOpeningTimeForDay(now);
        LocalDateTime closingToday = getClosingTimeForDay(now);
        OpeningTimeResult result = new OpeningTimeResult();
        result.isOpen = now.isAfter(openingToday) && now.isBefore(closingToday);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        if(result.isOpen) {
            result.closingTime = formatter.format(closingToday);
            result.openingTime = formatter.format(getOpeningTimeForDay(now.plusDays(1)));
        }else{
            result.openingTime = formatter.format(getOpeningTimeForDay(now.plusHours(12)));
            result.closingTime = formatter.format(getClosingTimeForDay(now.plusHours(12)));
        }

        return Response.ok(result).build();
    }
}
