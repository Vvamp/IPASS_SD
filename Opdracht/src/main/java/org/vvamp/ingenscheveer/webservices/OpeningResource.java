package org.vvamp.ingenscheveer.webservices;

import org.vvamp.ingenscheveer.models.api.OpeningTimeResult;
import org.vvamp.ingenscheveer.models.api.OperatingSchedule;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Path("operatingtimes")
public class OpeningResource {

    private ZonedDateTime getOpeningTimeForDay(ZonedDateTime date) {
        OperatingSchedule schedule = new OperatingSchedule();
        int day = date.getDayOfWeek().getValue()-1;
        LocalTime openingTime = schedule.getDays().get(day).getOpeningTime();
        ZonedDateTime openingToday = ZonedDateTime.of(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth(), openingTime.getHour(), openingTime.getMinute(), openingTime.getSecond(),0, ZoneId.of("Europe/Amsterdam"));
//        ZonedDateTime openingToday = ZonedDateTime.from(LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), openingTime.getHour(), openingTime.getMinute(), openingTime.getSecond()));
        return openingToday;
    }

    private ZonedDateTime getClosingTimeForDay(ZonedDateTime date) {
        OperatingSchedule schedule = new OperatingSchedule();
        int day = date.getDayOfWeek().getValue()-1;
        LocalTime closingTime = schedule.getDays().get(day).getClosingTime();
//        ZonedDateTime closingToday = ZonedDateTime.from(LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), closingTime.getHour(), closingTime.getMinute(), closingTime.getSecond()));
        ZonedDateTime closingToday = ZonedDateTime.of(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth(), closingTime.getHour(), closingTime.getMinute(), closingTime.getSecond(),0, ZoneId.of("Europe/Amsterdam"));

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
        var zonedUtc = now.atZone(ZoneId.of("UTC"));
        var zonedEurope = zonedUtc.withZoneSameInstant(ZoneId.of("Europe/Amsterdam"));

        ZonedDateTime openingToday = getOpeningTimeForDay(zonedEurope);
        ZonedDateTime closingToday = getClosingTimeForDay(zonedEurope);

        OpeningTimeResult result = new OpeningTimeResult();
        result.setOpen(zonedEurope.isAfter(openingToday) && zonedEurope.isBefore(closingToday));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        result.setNow(formatter.format(zonedEurope));

        if(result.isOpen()) {
            result.setClosingTime(formatter.format(closingToday));
            result.setOpeningTime(formatter.format(getOpeningTimeForDay(zonedEurope.plusDays(1))));
        }else{
            result.setOpeningTime(formatter.format(getOpeningTimeForDay(zonedEurope.plusHours(12))));
            result.setClosingTime(formatter.format(getClosingTimeForDay(zonedEurope.plusHours(12))));
        }

        return Response.ok(result).build();
    }
}
