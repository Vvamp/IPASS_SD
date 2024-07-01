package org.vvamp.ingenscheveer.webservices;

import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.models.Drukte;
import org.vvamp.ingenscheveer.models.api.DrukteCreateRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("drukte")
public class DrukteResource {

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDrukte(DrukteCreateRequest request) {
        Instant utc = Instant.now();

        Drukte dr = new Drukte(request.getDrukte(), LocalDateTime.ofInstant(utc, ZoneOffset.UTC));
        DatabaseStorageController.getDatabaseDrukteController().writeDrukte(dr);
        return Response.ok().build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrukteAll() {
        return Response.ok(Drukte.getAll()).build();
    }

    @GET

    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrukte() {
        Drukte drukte = Drukte.getAll().get(0);
        if(drukte == null) {
            drukte = new Drukte(-1, LocalDateTime.now());
        }
        return Response.ok(drukte).build();
    }
}
