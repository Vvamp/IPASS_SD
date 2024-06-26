package org.vvamp.ingenscheveer.webservices;

import org.vvamp.ingenscheveer.models.Statistics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/stats")
public class StatisticsController {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatistics() {
        Statistics statistics = new Statistics();

        return Response.ok(statistics).build();
    }

    @Path("/speed")
    public Response getCurrentSpeed(){
        Statistics statistics = new Statistics();

        return Response.ok(statistics.getCurrentSpeed()).build();
    }
}
