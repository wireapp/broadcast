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
package com.wire.bots.broadcast;

import com.wire.lithium.ClientRepo;
import com.wire.xenon.WireClient;
import com.wire.xenon.assets.MessageText;
import com.wire.xenon.exceptions.MissingStateException;
import com.wire.xenon.tools.Logger;
import org.jdbi.v3.core.Jdbi;

import java.util.UUID;

public class Broadcaster {
    private final String authToken;
    private final ClientRepo repo;
    private final Jdbi jdbi;

    public Broadcaster(String authToken, ClientRepo repo, Jdbi jdbi) {
        this.authToken = authToken;
        this.repo = repo;
        this.jdbi = jdbi;
    }

    public int broadcast(String text) {
        BotsDAO botsDAO = jdbi.onDemand(BotsDAO.class);

        int count = 0;
        for (UUID botId : botsDAO.getBots(authToken)) {
            try {
                WireClient client = repo.getClient(botId);
                client.send(new MessageText(text));
                count++;
            } catch (MissingStateException e) {
                Logger.warning("Bot previously deleted. Bot: %s", botId);
                botsDAO.delete(botId);
            } catch (Exception e) {
                Logger.error("broadcastText: %s Error: %s", botId, e);
            }
        }
        return count;
    }
}
