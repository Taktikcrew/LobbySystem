package de.taktikcrew.lobbysystem.lobbyplayer;

import de.chojo.sadu.mapper.wrapper.Row;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.query.Query;
import de.chojo.sadu.queries.call.adapter.UUIDAdapter;
import de.taktikcrew.lobbysystem.database.AbstractDatabaseDAO;
import de.taktikcrew.lobbysystem.gadgets.AbstractGadget;
import de.taktikcrew.lobbysystem.settings.AbstractSetting;
import de.taktikcrew.lobbysystem.settings.playerhider.PlayerHideState;
import de.taktikcrew.lobbysystem.settings.playerhider.PlayerHider;

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

        this.createDefaultSettings(lobbyPlayer);

        this.cache().put(lobbyPlayer.uuid(), lobbyPlayer);
    }

    public void createDefaultSettings(LobbyPlayer lobbyPlayer) {
        this.saveSetting(lobbyPlayer, new PlayerHider(lobbyPlayer.uuid(), PlayerHideState.SHOW_ALL));
    }

    public void saveSetting(LobbyPlayer lobbyPlayer, AbstractSetting<?> setting) {
        Query.query("INSERT INTO LobbyPlayer_settings (uuid, setting, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE state = VALUES(state)")
                .single(Call.of()
                        .bind(lobbyPlayer.uuid(), UUIDAdapter.AS_STRING)
                        .bind(setting.settingKey().key())
                        .bind(setting.serialize())
                )
                .insert();
    }

    public void saveGadget(LobbyPlayer lobbyPlayer, AbstractGadget gadget) {
        Query.query("INSERT INTO LobbyPlayer_gadgets (uuid, gadget, active) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE active = VALUES(active)")
                .single(Call.of()
                        .bind(lobbyPlayer.uuid(), UUIDAdapter.AS_STRING)
                        .bind(gadget.name())
                        .bind(gadget.active())
                )
                .insert();
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

        lobbyPlayer.settings().forEach(setting -> this.saveSetting(lobbyPlayer, setting));
        lobbyPlayer.gadgets().forEach(gadget -> this.saveGadget(lobbyPlayer, gadget));
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
