package de.taktikcrew.lobbysystem.listener.block;

import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class BlockPlaceListener implements Listener {

    public BlockPlaceListener(@NotNull Lobby lobby) {
        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onPlace(@NotNull BlockPlaceEvent event) {
        var player = event.getPlayer();
        event.setCancelled(!player.getGameMode().equals(GameMode.CREATIVE));
    }

}
