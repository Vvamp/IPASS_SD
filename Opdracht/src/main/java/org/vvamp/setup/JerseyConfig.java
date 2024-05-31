package org.vvamp.setup;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig
{
    public JerseyConfig() {
        packages("org.vvamp.ingenscheveer.webservices");

    }
}