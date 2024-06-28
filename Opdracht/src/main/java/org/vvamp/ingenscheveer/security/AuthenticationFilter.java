package org.vvamp.ingenscheveer.security;


import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.security.authentication.LoginManager;
import org.vvamp.ingenscheveer.security.authentication.ValidationResult;
import org.vvamp.ingenscheveer.security.authentication.ValidationStatus;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestCtx) {
        boolean isSecure = requestCtx.getSecurityContext().isSecure();
        String scheme = requestCtx.getUriInfo().getRequestUri().getScheme();
        // users are quests until valid jqt
        LoginManager loginManager = Main.loginManager;
        MySecurityContext msc = new MySecurityContext(null, scheme);
        String authHeader = requestCtx.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer".length()).trim();
            ValidationResult tokenResult = loginManager.checkTokenValidity(token);
            if (tokenResult.getStatus() == ValidationStatus.VALID) {
                msc = new MySecurityContext(tokenResult.getUser(), scheme);
                System.out.println("Successs");
            }else{
                System.out.println("Invalid token");
            }

        }
        requestCtx.setSecurityContext(msc);
    }
}