package de.taktikcrew.lobbysystem.database;

import de.chojo.sadu.mapper.wrapper.Row;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.query.Query;
import de.chojo.sadu.queries.call.adapter.UUIDAdapter;
import de.taktikcrew.lobbysystem.objects.LobbyPlayer;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class LobbyPlayerDAO extends AbstractDatabaseDAO<LobbyPlayer, UUID> {

    public LobbyPlayerDAO() {
        super("LobbyPlayer");
    }

    @Override
    public void create(LobbyPlayer lobbyPlayer) {
        if (this.exists(lobbyPlayer.uuid())) {
            return;
        }

        Query.query("INSERT INTO LobbyPlayer (uuid, dsgvoAccepted) VALUES (?, ?)")
                .single(Call.of()
                        .bind(lobbyPlayer.uuid(), UUIDAdapter.AS_STRING)
                        .bind(false)
                )
                .insert();

        this.cache().put(lobbyPlayer.uuid(), lobbyPlayer);
    }

    @Override
    public void update(LobbyPlayer lobbyPlayer) {
        if (!this.exists(lobbyPlayer.uuid())) {
            this.create(lobbyPlayer);
            return;
        }

        Query.query("UPDATE LobbyPlayer SET dsgvoAccepted = ? WHERE uuid = ?")
                .single(Call.of()
                        .bind(lobbyPlayer.dsgvoAccepted())
                        .bind(lobbyPlayer.uuid(), UUIDAdapter.AS_STRING)
                )
                .update();
    }

    @Override
    public void delete(UUID uuid) {}

    @Override
    protected LobbyPlayer map(Row row) throws SQLException {
        return new LobbyPlayer(row);
    }

    @Override
    protected Optional<LobbyPlayer> getFromDatabase(UUID uuid) {
        return Query.query("SELECT * FROM LobbyPlayer WHERE uuid = ?")
                .single(Call.of().bind(uuid, UUIDAdapter.AS_STRING))
                .map(this::map)
                .first();
    }

    @Override
    protected boolean existsInDatabase(UUID uuid) {
        return Query.query("SELECT COUNT(*) as count FROM LobbyPlayer WHERE uuid = ?")
                .single(Call.of().bind(uuid, UUIDAdapter.AS_STRING))
                .map(row -> row.getInt("count") > 0)
                .first()
                .orElse(false);
    }
}
