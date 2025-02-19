package org.vvamp.ingenscheveer.webservices;


import org.vvamp.ingenscheveer.models.Ferry;
import org.vvamp.ingenscheveer.models.FerryCrossing;
import org.vvamp.ingenscheveer.models.api.FerryCrossingEtaResult;
import org.vvamp.ingenscheveer.models.api.PaginatedResponse;

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
            crossings = crossings.stream().limit(limit)
//                    .map(i -> new FerryCrossing(i.getDeparture(), i.getArrival(), null))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("Error", "Failed to retrieve FerryCrossing data")).build();
        }

        return Response.status(Response.Status.OK).entity(crossings).build();
    }

    @GET
    @Path("today")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCrossingsToday(@QueryParam("limit") @DefaultValue("-1") int limit,
                                      @QueryParam("page") @DefaultValue("-1") int page) {
        try {
            List<FerryCrossing> crossings;
            crossings = Ferry.getFerry().getFerryCrossings();
            Collections.reverse(crossings);

            // Define start and end timestamps for today
            LocalDateTime now = LocalDateTime.now().minusDays(1);
            LocalDateTime start = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime end = now.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
            long startTS = start.toEpochSecond(ZoneOffset.UTC);
            long endTS = end.toEpochSecond(ZoneOffset.UTC);

            // Filter crossings once and store in a list to avoid consuming the stream
            List<FerryCrossing> filteredCrossings = crossings.stream()
                    .filter(c -> c.getDeparture().getEpochSeconds() >= startTS && c.getDeparture().getEpochSeconds() <= endTS)
                    .map(i -> new FerryCrossing(i.getDeparture(), i.getArrival(), null))
                    .collect(Collectors.toList());

            int count = filteredCrossings.size(); // Get the total count after filtering

            // Apply pagination if required
            if (limit > 0 && page >= 0) {
                int fromIndex = Math.min(limit * page, count);
                int toIndex = Math.min(fromIndex + limit, count);
                filteredCrossings = filteredCrossings.subList(fromIndex, toIndex);
            } else if (limit > 0) {
                filteredCrossings = filteredCrossings.stream().limit(limit).collect(Collectors.toList());
            }

            return Response.status(Response.Status.OK)
                    .entity(new PaginatedResponse<>(filteredCrossings, count))
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to retrieve FerryCrossing data"))
                    .build();
        }
    }


    @GET
    @Path("latest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCrossings() {
        List<FerryCrossing> crossings = Ferry.getFerry().getFerryCrossings();
        FerryCrossing latestCrossing = crossings.get(crossings.size()-1);

        FerryCrossing sanitizedCrossing = new FerryCrossing(latestCrossing.getDeparture(), latestCrossing.getArrival(), null);

        return Response.status(Response.Status.OK).entity(sanitizedCrossing).build();
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
        res.setEta(latestCrossing.getDeparture().getEpochSeconds() + 150);
        return Response.status(Response.Status.OK).entity(res).build();

    }


}
