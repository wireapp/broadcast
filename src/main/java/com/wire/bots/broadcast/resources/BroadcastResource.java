//
// Wire
// Copyright (C) 2016 Wire Swiss GmbH
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see http://www.gnu.org/licenses/.
//
package com.wire.bots.broadcast.resources;

import com.codahale.metrics.annotation.Timed;
import com.wire.bots.broadcast.Broadcaster;
import com.wire.bots.broadcast.Service;
import com.wire.bots.broadcast.model.Simple;
import com.wire.lithium.ClientRepo;
import com.wire.xenon.tools.Logger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api
@Path("/broadcast")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BroadcastResource {
    private final Broadcaster broadcaster;

    public BroadcastResource(ClientRepo repo, Jdbi jdbi) {
        broadcaster = new Broadcaster(Service.instance.getConfig().tokens.fraktionruf, repo, jdbi);
    }

    @POST
    @Timed
    @ApiOperation(value = "Broadcast message on Wire")
    @Authorization("Bearer")
    public Response webhook(@ApiParam("Service token Bearer") @HeaderParam(HttpHeaders.AUTHORIZATION) String auth,
                            @ApiParam @NotNull @Valid Simple payload) {
        try {

            int broadcast = broadcaster.broadcast(payload.message);

            Logger.info("BroadcastResource: New payload texted in %d convs", broadcast);

            return Response.
                    accepted().
                    build();
        } catch (Exception e) {
            Logger.error("BroadcastResource: %s", e);
            return Response.
                    serverError().
                    build();
        }
    }
}

