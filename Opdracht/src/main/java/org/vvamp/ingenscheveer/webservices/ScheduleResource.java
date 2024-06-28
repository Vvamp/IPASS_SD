package org.vvamp.ingenscheveer.webservices;

import org.vvamp.ingenscheveer.models.schedule.GroupedSchedule;
import org.vvamp.ingenscheveer.models.schedule.Schedule;
import org.vvamp.ingenscheveer.security.MySecurityContext;
import org.vvamp.ingenscheveer.security.authentication.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Path("/schedules")
public class ScheduleResource {

    @GET
//    @RolesAllowed({"skipper", "admin", "boss"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchedule(@QueryParam("weeknr") int weeknr) {

        System.out.println("Get weeknumber: " + weeknr);
        return Response.ok(GroupedSchedule.getGlobalScheduleGrouped(weeknr)).build();
    }

    @GET
    @RolesAllowed({"skipper", "boss"})
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScheduleForUser(@PathParam("username") String username, @Context ContainerRequestContext requestCtx) {
        MySecurityContext sc = (MySecurityContext) requestCtx.getSecurityContext(); // simply passing MySecurityContext as param didn't work(it was null)
        if (sc == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (!(sc.getUserPrincipal() instanceof User user) || sc.getUserPrincipal().getName().equals(username) == false) return Response.status(Response.Status.FORBIDDEN).build();

        User currentUser = User.getUserByName(username);
        if (currentUser == null) return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.ok(currentUser.getSchedule()).build();

    }

}
