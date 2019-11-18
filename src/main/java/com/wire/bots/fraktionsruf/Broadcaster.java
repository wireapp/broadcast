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
package com.wire.bots.fraktionsruf;

import com.wire.bots.sdk.ClientRepo;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.exceptions.MissingStateException;
import com.wire.bots.sdk.tools.Logger;
import org.skife.jdbi.v2.DBI;

import java.util.UUID;

public class Broadcaster {
    private final ClientRepo repo;
    private final DBI jdbi;

    public Broadcaster(ClientRepo repo, DBI jdbi) {
        this.repo = repo;
        this.jdbi = jdbi;
    }

    public int broadcast(String text) {
        BotsDAO botsDAO = jdbi.onDemand(BotsDAO.class);

        int count = 0;
        for (UUID botId : botsDAO.getBots()) {
            try {
                WireClient client = repo.getClient(botId);
                client.sendText(text);
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
