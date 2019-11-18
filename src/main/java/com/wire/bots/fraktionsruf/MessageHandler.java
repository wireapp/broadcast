package com.wire.bots.fraktionsruf;

import com.wire.bots.sdk.MessageHandlerBase;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.server.model.NewBot;
import com.wire.bots.sdk.server.model.SystemMessage;
import com.wire.bots.sdk.tools.Logger;
import org.skife.jdbi.v2.DBI;

import java.util.UUID;

public class MessageHandler extends MessageHandlerBase {
    private final BotsDAO botsDAO;

    MessageHandler(DBI jdbi) {
        this.botsDAO = jdbi.onDemand(BotsDAO.class);
    }

    @Override
    public boolean onNewBot(NewBot newBot) {
        if (1 == botsDAO.insert(newBot.id))
            Logger.info("onNewBot. New subscriber, bot: %s, user: %s", newBot.id, newBot.origin.id);

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
        botsDAO.delete(botId);
    }
}
