package com.wire.bots.broadcast;

import com.wire.xenon.MessageHandlerBase;
import com.wire.xenon.WireClient;
import com.wire.xenon.assets.MessageText;
import com.wire.xenon.backend.models.NewBot;
import com.wire.xenon.backend.models.SystemMessage;
import com.wire.xenon.tools.Logger;
import org.jdbi.v3.core.Jdbi;

import java.util.UUID;

public class MessageHandler extends MessageHandlerBase {
    private final Jdbi jdbi;

    MessageHandler(Jdbi jdbi) {
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
            client.send(new MessageText("Hello"));
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
