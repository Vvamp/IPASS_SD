package org.vvamp.ingenscheveer.webservices;

import io.jsonwebtoken.JwtException;
import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.models.api.LoginResult;
import org.vvamp.ingenscheveer.security.authentication.LoginManager;
import org.vvamp.ingenscheveer.security.authentication.ValidationResult;
import org.vvamp.ingenscheveer.security.authentication.ValidationStatus;
import org.vvamp.ingenscheveer.models.api.LogonRequest;
import org.vvamp.ingenscheveer.models.api.TokenRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Path("/user")
public class LoginResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response authenticateUser(LogonRequest request) {
        try {
            LoginManager loginManager = Main.loginManager;
            String role = loginManager.validateLogin(request.user, request.password);
            if (role == null) return Response.status(Response.Status.UNAUTHORIZED).build();
            String token = loginManager.createToken(request.user, role);
            loginManager.validateToken(token);

            LoginResult lr = new LoginResult(token, request.user, role);

            return Response.ok(lr).build();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("Error", "Failed to authenticate")).build();
        }
    }

    // validate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/validate")
    public Response validateToken(@QueryParam("token") String token) {
        if (token.isEmpty()) return Response.status(422).build();
        LoginManager loginManager = Main.loginManager;
        ValidationResult result = loginManager.checkTokenValidity(token);
        Map<String, Object> response = new HashMap<>();
        response.put("isValid", result.getStatus() == ValidationStatus.VALID);
        response.put("details", result.getDetails());
        response.put("token", token);
        System.out.println("Valid? " + (result.getStatus() == ValidationStatus.VALID));
        return Response.ok().entity(response).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/logout")
    public Response logoutUser(TokenRequest request) {
        LoginManager loginManager = Main.loginManager;
        loginManager.invalidateToken(request.token);
        return Response.ok().entity(Map.of("Success", "You are now logged out")).build();
    }
}
