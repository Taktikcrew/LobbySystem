package de.taktikcrew.lobbysystem.listener.player;

import de.smoofy.core.api.Core;
import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class PlayerGameModeChangeListener implements Listener {

    private final Lobby lobby;

    public PlayerGameModeChangeListener(Lobby lobby) {
        this.lobby = lobby;

        this.lobby.getServer().getPluginManager().registerEvents(this, this.lobby);
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if (!event.getNewGameMode().equals(GameMode.SURVIVAL)) {
            return;
        }
        var corePlayer = Core.instance().corePlayerProvider().corePlayer(event.getPlayer());
        this.lobby.inventoryProvider().lobbyPlayerInventory().setLobbyInventory(corePlayer);
    }
}