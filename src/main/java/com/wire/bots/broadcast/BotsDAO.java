package com.wire.bots.broadcast;

import com.wire.xenon.tools.Logger;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

public interface BotsDAO {

    @SqlQuery("SELECT bot_id AS uuid FROM Bots WHERE service_auth = :serviceAuth")
    @RegisterColumnMapper(_Mapper.class)
    List<UUID> getBots(@Bind("serviceAuth") String serviceAuth);

    @SqlUpdate("INSERT INTO Bots(bot_id, service_auth) VALUES (:botId, :serviceAuth)")
    int insert(@Bind("botId") UUID botId,
               @Bind("serviceAuth") String serviceAuth);

    @SqlUpdate("DELETE FROM Bots WHERE bot_id = :botId")
    void delete(@Bind("botId") UUID botId);

    class _Mapper implements ColumnMapper<UUID> {
        @Override
        public UUID map(ResultSet rs, int columnNumber, StatementContext ctx) {
            try {
                return getUuid(rs);
            } catch (SQLException e) {
                Logger.error("User2BotDAOMapper: i: %d, e: %s", columnNumber, e);
                return null;
            }
        }

        private UUID getUuid(ResultSet rs) throws SQLException {
            UUID contact = null;
            Object rsObject = rs.getObject("uuid");
            if (rsObject != null)
                contact = (UUID) rsObject;
            return contact;
        }
    }
}
