package org.vvamp.ingenscheveer.webservices;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.vvamp.ingenscheveer.CrossingController;
import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.models.FerryCrossing;
import org.vvamp.ingenscheveer.models.StatusUpdate;
import org.vvamp.ingenscheveer.models.json.MainMessage;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Path("/crossings")
public class FerryCrossingResource {
    @GET
    @Produces("application/json")
    public Response getCrossings() {
        CrossingController crossingController = new CrossingController();
        ArrayList<MainMessage> shipMessages = Main.storageController.load();
        ArrayList<StatusUpdate> statusUpdates = crossingController.getStatusUpdates(shipMessages);
        ArrayList<FerryCrossing> crossings = crossingController.getFerryCrossings(statusUpdates);


        if(crossings.size() == 0){
            return Response.status(200).entity("").build();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(crossings);
            return Response.status(200).entity(json).build();
        }catch(Exception e){
            Map<String,String> messages = new HashMap<>();
            messages.put("Error", "Failed to generate response data");
            return Response.status(500).entity(messages).build();

        }
    }


}
