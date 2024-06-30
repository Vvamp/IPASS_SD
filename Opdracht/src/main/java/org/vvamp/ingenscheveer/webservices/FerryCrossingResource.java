package org.vvamp.ingenscheveer.webservices;


import org.vvamp.ingenscheveer.models.Ferry;
import org.vvamp.ingenscheveer.models.FerryCrossing;
import org.vvamp.ingenscheveer.models.api.FerryCrossingEtaResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/crossings")
public class FerryCrossingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCrossings(@QueryParam("limit") @DefaultValue("-1") int limit) {

        List<FerryCrossing> crossings;
        try {
            if(limit <= -1){
                limit = 50;
            }
            crossings = Ferry.getFerry().getFerryCrossings();
            Collections.reverse(crossings);
            crossings = crossings.stream().limit(limit).collect(Collectors.toList());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("Error", "Failed to retrieve FerryCrossing data")).build();
        }

        return Response.status(Response.Status.OK).entity(crossings).build();
    }

    @GET
    @Path("latest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCrossings() {
        List<FerryCrossing> crossings = Ferry.getFerry().getFerryCrossings();
        FerryCrossing latestCrossing = crossings.get(crossings.size() - 1);


        return Response.status(Response.Status.OK).entity(latestCrossing).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("eta")
    public Response getEta() {
        List<FerryCrossing> crossings = Ferry.getFerry().getFerryCrossings();
        FerryCrossing latestCrossing = crossings.get(crossings.size() - 1);

        var res = new FerryCrossingEtaResult();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        ZonedDateTime datetime = ZonedDateTime.ofInstant(
//                Instant.ofEpochSecond(latestCrossing.getDeparture().getEpochSeconds()),
//                ZoneId.systemDefault()
//        ).plusMinutes(2).plusSeconds(30);
        res.eta = latestCrossing.getDeparture().getEpochSeconds() + 150;
        return Response.status(Response.Status.OK).entity(res).build();

    }


}
