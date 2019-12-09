package com.wire.bots.broadcast;

import com.wire.bots.sdk.tools.Logger;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface BotsDAO {

    @SqlQuery("SELECT bot_id AS uuid FROM Bots WHERE service_auth = :serviceAuth")
    @RegisterMapper(_Mapper.class)
    List<UUID> getBots(@Bind("serviceAuth") String serviceAuth);

    @SqlUpdate("INSERT INTO Bots(bot_id, service_auth) VALUES (:botId, :serviceAuth)")
    int insert(@Bind("botId") UUID botId,
               @Bind("serviceAuth") String serviceAuth);

    @SqlUpdate("DELETE FROM Bots WHERE bot_id = :botId")
    int delete(@Bind("botId") UUID botId);

    class _Mapper implements ResultSetMapper<UUID> {
        @Override
        @Nullable
        public UUID map(int i, ResultSet rs, StatementContext statementContext) {
            try {
                return getUuid(rs, "uuid");
            } catch (SQLException e) {
                Logger.error("User2BotDAOMapper: i: %d, e: %s", i, e);
                return null;
            }
        }

        private UUID getUuid(ResultSet rs, String name) throws SQLException {
            UUID contact = null;
            Object rsObject = rs.getObject(name);
            if (rsObject != null)
                contact = (UUID) rsObject;
            return contact;
        }
    }
}
