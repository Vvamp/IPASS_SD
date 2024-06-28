package org.vvamp.ingenscheveer.webservices;

import org.vvamp.ingenscheveer.models.schedule.Schedule;
import org.vvamp.ingenscheveer.security.MySecurityContext;
import org.vvamp.ingenscheveer.security.authentication.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/schedules")
public class ScheduleResource {

    @GET
    @RolesAllowed({"skipper", "admin", "boss"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchedule() {
        return Response.ok(Schedule.getGlobalSchedule()).build();
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
