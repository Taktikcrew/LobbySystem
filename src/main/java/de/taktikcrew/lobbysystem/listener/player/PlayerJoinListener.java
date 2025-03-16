package de.taktikcrew.lobbysystem.listener.player;

import de.smoofy.core.api.Core;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.database.LobbyPlayerDAO;
import de.taktikcrew.lobbysystem.objects.LobbyPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Lobby lobby;
    private final LobbyPlayerDAO lobbyPlayerDAO;

    public PlayerJoinListener(Lobby lobby) {
        this.lobby = lobby;
        this.lobbyPlayerDAO = this.lobby.lobbyPlayerDAO();

        this.lobby.getServer().getPluginManager().registerEvents(this, this.lobby);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        this.lobbyPlayerDAO.create(new LobbyPlayer(player.getUniqueId(), false));
        var optionalLobbyPlayer = this.lobbyPlayerDAO.get(player.getUniqueId());
        optionalLobbyPlayer.ifPresent(lobbyPlayer -> {
            if (!lobbyPlayer.dsgvoAccepted()) {
                Core.instance().coreTask().later(() -> player.openInventory(this.lobby.dsgvoInventory().inventory()), 1);
            }
        });
    }
}