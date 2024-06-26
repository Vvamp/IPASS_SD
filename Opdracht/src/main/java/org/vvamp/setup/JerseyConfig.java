package org.vvamp.setup;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig
{
    public JerseyConfig() {
        packages("org.vvamp.ingenscheveer.webservices, nl.hu.bep.ingenscheveer.security");
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
        register(RolesAllowedDynamicFeature.class);
    }
}