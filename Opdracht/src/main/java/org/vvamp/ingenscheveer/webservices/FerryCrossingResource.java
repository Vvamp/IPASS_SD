package org.vvamp.ingenscheveer.webservices;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.vvamp.ingenscheveer.CrossingController;
import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.models.Ferry;
import org.vvamp.ingenscheveer.models.FerryCrossing;
import org.vvamp.ingenscheveer.models.StatusUpdate;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/crossings")
public class FerryCrossingResource {
    @GET
    @Produces("application/json")
    public Response getCrossings() {
       ArrayList<FerryCrossing> crossings = Ferry.getFerry().getFerryCrossings();


        if(crossings.size() == 0){
            return Response.status(200).entity("").build();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(crossings);
            return Response.status(200).entity(crossings).build();
        }catch(Exception e){
            Map<String,String> messages = new HashMap<>();
            messages.put("Error", "Failed to generate response data");
            return Response.status(500).entity(messages).build();

        }
    }


}
