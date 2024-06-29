package org.vvamp.ingenscheveer.webservices;


import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/ais")
public class AISResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpdates(@QueryParam("limit") @DefaultValue("-1") int limit) {
        List<AisSignal> shipMessages;
        try {
            shipMessages = DatabaseStorageController.getDatabaseAisController().getAllAisSignals();
            Collections.reverse(shipMessages);
            if (limit > -1) {
                shipMessages = shipMessages.stream().limit(limit).collect(Collectors.toList());
            }

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("Error", "Failed to generate response data")).build();
        }
        return Response.status(Response.Status.OK).entity(shipMessages).build();
    }

}
