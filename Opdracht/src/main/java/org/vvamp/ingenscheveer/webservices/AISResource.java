package org.vvamp.ingenscheveer.webservices;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Path("/ais")
public class AISResource {
    @GET
    @Produces("application/json")
    public Response getShip() {
        ArrayList<AisSignal> shipMessages = Main.storageController.load();

        if(shipMessages.size() == 0){
            return Response.status(200).entity("").build();
        }
        AisSignal mostRecentUpdate = shipMessages.get(shipMessages.size()-1);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(mostRecentUpdate);
            return Response.status(200).entity(json).build();
        }catch(Exception e){
            Map<String,String> messages = new HashMap<>();
            messages.put("Error", "Failed to generate response data");
            return Response.status(500).entity(messages).build();

        }
    }

    @GET
    @Path("/updates")
    @Produces("application/json")
    public Response getUpdates() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        ArrayList<AisSignal> shipMessages = Main.storageController.load();

        if(shipMessages.size() == 0){
            return Response.status(200).entity("").build();
        }

        ObjectMapper mapper = new ObjectMapper();

            try {
                String json = mapper.writeValueAsString(shipMessages);
                return Response.status(200).entity(json).build();

            } catch (JsonProcessingException e) {
                Map<String,String> messages = new HashMap<>();
                messages.put("Error", "Failed to generate response data");
                return Response.status(500).entity(messages).build();
            }
    }

    @GET
    @Path("/updates/{count}")
    @Produces("application/json")
    public Response getUpdatesWithCount(@PathParam("count") int count) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        ArrayList<AisSignal> shipMessages = Main.storageController.load();

        if(shipMessages.size() == 0){
            return Response.status(200).entity("").build();
        }

        int actualCount = count <= shipMessages.size() ? count : shipMessages.size();
        Collections.reverse(shipMessages); // from new to old
        List<AisSignal> truncatedMessages = shipMessages.stream().limit(actualCount).collect(Collectors.toList()); // limit to 'count' newest

        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(truncatedMessages);
            return Response.status(200).entity(json).build();

        } catch (JsonProcessingException e) {
            Map<String,String> messages = new HashMap<>();
            messages.put("Error", "Failed to generate response data");
            return Response.status(500).entity(messages).build();
        }
    }
}
