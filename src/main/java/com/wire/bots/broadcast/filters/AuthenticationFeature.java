package com.wire.bots.broadcast.filters;

import io.swagger.annotations.Authorization;

import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuthenticationFeature implements DynamicFeature {
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (resourceInfo.getResourceMethod().getAnnotation(Authorization.class) != null) {
            context.register(AuthenticationFilter.class);
        }
    }
}
