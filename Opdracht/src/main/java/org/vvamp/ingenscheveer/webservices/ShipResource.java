package org.vvamp.ingenscheveer.webservices;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.vvamp.ingenscheveer.WebSocketClient;
import org.vvamp.ingenscheveer.models.MainMessage;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/ship")
public class ShipResource {
    @GET
    @Produces("application/json")
    public String getShip() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        if(WebSocketClient.shipMessages.size() == 0){
            return "No ship updates";
        }
        MainMessage mostRecentUpdate = WebSocketClient.shipMessages.get(WebSocketClient.shipMessages.size()-1);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(mostRecentUpdate);
            return json;
        }catch(Exception e){
            return "Failed to serialize the most recent update";
        }
//        JsonArray array = jab.build();
//        return array.toString();
    }
}
