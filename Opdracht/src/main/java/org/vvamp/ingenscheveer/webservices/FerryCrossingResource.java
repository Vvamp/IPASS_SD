package org.vvamp.ingenscheveer.webservices;


import org.vvamp.ingenscheveer.models.Ferry;
import org.vvamp.ingenscheveer.models.FerryCrossing;
import org.vvamp.ingenscheveer.models.api.FerryCrossingEtaResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
            crossings = Ferry.getFerry().getFerryCrossings();
            Collections.reverse(crossings);
            if (limit > -1) {
                crossings = crossings.stream().limit(limit).collect(Collectors.toList());
            }
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
        FerryCrossing latestCrossing = null;
        for (FerryCrossing crossing : crossings) {
            if(latestCrossing == null) {
                latestCrossing = crossing;
                continue;
            }
            if(crossing.getDeparture().getEpochSeconds() > latestCrossing.getDeparture().getEpochSeconds()) {
                latestCrossing = crossing;
            }
        }

        return Response.status(Response.Status.OK).entity(latestCrossing).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("eta")
    public Response getEta() {
        List<FerryCrossing> crossings = Ferry.getFerry().getFerryCrossings();
        FerryCrossing latestCrossing = null;
        for (FerryCrossing crossing : crossings) {
            if(latestCrossing == null) {
                latestCrossing = crossing;
                continue;
            }
            if(crossing.getDeparture().getEpochSeconds() > latestCrossing.getDeparture().getEpochSeconds()) {
                latestCrossing = crossing;
            }
        }
        var res = new FerryCrossingEtaResult();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime datetime = LocalDateTime.ofEpochSecond(latestCrossing.getDeparture().getEpochSeconds(), 0, ZoneOffset.UTC).plusMinutes(2).plusSeconds(30);
        res.eta = datetime.format(formatter);
        return Response.status(Response.Status.OK).entity(res).build();

    }


}
