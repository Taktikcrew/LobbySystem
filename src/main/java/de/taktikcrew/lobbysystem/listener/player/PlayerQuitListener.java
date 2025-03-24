package de.taktikcrew.lobbysystem.listener.player;

import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.database.LobbyPlayerDAO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

    private final LobbyPlayerDAO lobbyPlayerDAO;

    public PlayerQuitListener(@NotNull Lobby lobby) {
        this.lobbyPlayerDAO = lobby.lobbyPlayerDAO();

        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent event) {
        var player = event.getPlayer();
        var optionalLobbyPlayer = this.lobbyPlayerDAO.get(player.getUniqueId());
        optionalLobbyPlayer.ifPresent(this.lobbyPlayerDAO::update);
    }
}