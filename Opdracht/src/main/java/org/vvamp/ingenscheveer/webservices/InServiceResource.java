package org.vvamp.ingenscheveer.webservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.models.json.MainMessage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Path("/service")
public class InServiceResource {
    @GET
    @Produces("application/json")
    public Response getIsOpen() {

            return null;
        }
    }

