package com.wire.bots.broadcast.filters;

import com.wire.bots.broadcast.Service;
import com.wire.bots.broadcast.model.Config;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Objects;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String auth = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (auth == null) {
            Exception cause = new IllegalArgumentException("Missing Authorization");
            throw new WebApplicationException(cause, Response.Status.BAD_REQUEST);
        }

        String[] split = auth.split(" ");

        if (split.length != 2) {
            Exception cause = new IllegalArgumentException("Bad Authorization");
            throw new WebApplicationException(cause, Response.Status.BAD_REQUEST);
        }

        String type = split[0];
        String token = split[1];

        if (!Objects.equals(type, "Bearer")) {
            Exception cause = new IllegalArgumentException("Bad Authorization");
            throw new WebApplicationException(cause, Response.Status.BAD_REQUEST);
        }

        Config config = Service.instance.getConfig();

        String confluence = config.tokens.confluence;
        String fraktionruf = config.tokens.fraktionruf;

        if (!Objects.equals(token, confluence) && !Objects.equals(token, fraktionruf)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        requestContext.setProperty("wire-auth", token);
    }
}