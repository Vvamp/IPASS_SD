package org.vvamp.ingenscheveer.webservices;

import org.vvamp.ingenscheveer.models.api.TaskCreateRequest;
import org.vvamp.ingenscheveer.models.api.TaskDeleteRequest;
import org.vvamp.ingenscheveer.models.schedule.GroupedSchedule;
import org.vvamp.ingenscheveer.models.schedule.Schedule;
import org.vvamp.ingenscheveer.models.schedule.ScheduleTask;
import org.vvamp.ingenscheveer.models.schedule.TaskType;
import org.vvamp.ingenscheveer.security.MySecurityContext;
import org.vvamp.ingenscheveer.security.authentication.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Path("/schedules")
public class ScheduleResource {

    @GET
    @RolesAllowed({"skipper", "admin", "boss"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchedule(@QueryParam("weeknr") int weeknr) {
        return Response.ok(GroupedSchedule.getGlobalScheduleGrouped(weeknr)).build();
    }


    @GET
    @RolesAllowed({"skipper", "boss"})
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScheduleForUser(@PathParam("username") String username, @QueryParam("weeknr") int weeknr, @Context ContainerRequestContext requestCtx) {
        MySecurityContext sc = (MySecurityContext) requestCtx.getSecurityContext(); // simply passing MySecurityContext as param didn't work(it was null)
        if (sc == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (!(sc.getUserPrincipal() instanceof User user) || !sc.getUserPrincipal().getName().equals(username))
            return Response.status(Response.Status.FORBIDDEN).build();

        User currentUser = User.getUserByName(username);
        if (currentUser == null) return Response.status(Response.Status.BAD_REQUEST).build();


        return Response.ok(GroupedSchedule.getGlobalScheduleGroupedForUser(weeknr, currentUser)).build();

    }

    @POST
    @Path("/add")
    @RolesAllowed("boss")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addScheduleItemForUser(TaskCreateRequest request){
        User user = User.getUserByName(request.username);
        if (user == null) return Response.status(Response.Status.BAD_REQUEST).build();
        Schedule schedule = user.getSchedule();
        if (schedule == null) return Response.status(Response.Status.BAD_REQUEST).build();

        TaskType type;
        if(request.role.equals("dienst")) {
            type = TaskType.Dienst;
        }else{
            type = TaskType.Kniphulp;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime begin = LocalDateTime.parse(request.start, formatter);
        LocalDateTime end = LocalDateTime.parse(request.end, formatter);
        ScheduleTask scheduleTask = new ScheduleTask(begin, end, "", user, type);
        schedule.scheduleTask(scheduleTask);
        return Response.ok().build();

    }

    @DELETE
    @Path("/delete")
    @RolesAllowed("boss")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteSchedule(TaskDeleteRequest request) {
        var test = Schedule.getGlobalSchedule();
        ScheduleTask task = Schedule.getGlobalSchedule().getTaskById(request.uuid);
        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        User target = User.getUserByName(task.getUsername());
        if (target == null) return Response.status(Response.Status.BAD_REQUEST).build();

        target.getSchedule().removeTask(task.getUuid());
        return Response.ok().build();

    }

}
