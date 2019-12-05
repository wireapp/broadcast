package com.wire.bots.broadcast;

import com.wire.bots.sdk.MessageHandlerBase;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.server.model.NewBot;
import com.wire.bots.sdk.server.model.SystemMessage;
import com.wire.bots.sdk.tools.Logger;
import org.skife.jdbi.v2.DBI;

import java.util.UUID;

public class MessageHandler extends MessageHandlerBase {
    private final DBI jdbi;

    MessageHandler(DBI jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public boolean onNewBot(NewBot newBot, String serviceAuth) {
        BotsDAO botsDAO = jdbi.onDemand(BotsDAO.class);
        if (1 == botsDAO.insert(newBot.id, serviceAuth))
            Logger.info("onNewBot (%s). New subscriber, bot: %s, user: %s", serviceAuth, newBot.id, newBot.origin.id);
        return true;
    }

    @Override
    public void onNewConversation(WireClient client, SystemMessage message) {
        UUID botId = client.getId();
        try {
            client.sendText("Hello");
        } catch (Exception e) {
            Logger.error("onNewConversation: %s %s", botId, e);
        }
    }

    @Override
    public void onBotRemoved(UUID botId, SystemMessage msg) {
        Logger.info("onBotRemoved. subscriber, bot: %s", botId);
        BotsDAO botsDAO = jdbi.onDemand(BotsDAO.class);
        botsDAO.delete(botId);
    }
}
