package org.vvamp.ingenscheveer.webservices;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/orders")
public class OrderResource {
    @GET
    @Produces("application/json")
    public String getOrders() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (int i = 0; i < 10; i++) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("id", i);
            job.add("customer", "Vincent");
            jab.add(job);
        }
        JsonArray array = jab.build();
        return array.toString();
    }
}
