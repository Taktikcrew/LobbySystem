package de.taktikcrew.lobbysystem.listener.player;

import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDropItemListener implements Listener {

    public PlayerDropItemListener(@NotNull Lobby lobby) {
        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onDrop(@NotNull PlayerDropItemEvent event) {
        event.setCancelled(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE));
    }
}