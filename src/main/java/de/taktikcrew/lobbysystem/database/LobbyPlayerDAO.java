package de.taktikcrew.lobbysystem.database;

import de.taktikcrew.lobbysystem.objects.LobbyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LobbyPlayerDAO extends AbstractDatabaseDAO<LobbyPlayer, UUID> {

    public LobbyPlayerDAO() {
        super(LobbyPlayer.class, "uuid");
    }

    @Override
    public void create(@NotNull LobbyPlayer lobbyPlayer) {
        if (this.exists(lobbyPlayer.uuid())) {
            return;
        }
        this.repository().query().create(lobbyPlayer);
        this.cache().put(lobbyPlayer.uuid(), lobbyPlayer);
    }

    @Override
    public void update(@NotNull LobbyPlayer lobbyPlayer) {
        this.repository().query().match(this.id(), lobbyPlayer.uuid()).update(lobbyPlayer);
    }
}
