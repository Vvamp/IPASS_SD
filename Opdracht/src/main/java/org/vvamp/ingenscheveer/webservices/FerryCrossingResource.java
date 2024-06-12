package org.vvamp.ingenscheveer.webservices;


import org.vvamp.ingenscheveer.models.Ferry;
import org.vvamp.ingenscheveer.models.FerryCrossing;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
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


}
