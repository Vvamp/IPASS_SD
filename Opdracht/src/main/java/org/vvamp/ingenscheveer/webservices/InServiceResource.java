package org.vvamp.ingenscheveer.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/service")
public class InServiceResource {
    @GET
    @Produces("application/json")
    public Response getIsOpen() {

            return null;
        }
    }

