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
import com.wire.bots.sdk.ClientRepo;
import com.wire.bots.sdk.tools.Logger;
import com.wire.bots.sdk.tools.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.skife.jdbi.v2.DBI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/broadcast")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BroadcastResource {
    private final Broadcaster broadcaster;

    public BroadcastResource(ClientRepo repo, DBI jdbi) {
        broadcaster = new Broadcaster(repo, jdbi);
    }

    @POST
    @Timed
    @ApiOperation(value = "Broadcast message on Wire")
    public Response webhook(@ApiParam("Bearer token") @NotNull @Valid @HeaderParam("Authorization") String token,
                            @ApiParam @NotNull @Valid Simple payload) {

        try {
            if (!Util.compareAuthorizations(token, Service.instance.getConfig().secret)) {
                Logger.warning("BroadcastResource: Wrong Authorization: %s", token);
                return Response.
                        status(401).
                        build();
            }

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

