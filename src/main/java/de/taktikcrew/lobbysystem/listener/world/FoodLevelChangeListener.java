package de.taktikcrew.lobbysystem.listener.world;

import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

public class FoodLevelChangeListener implements Listener {

    public FoodLevelChangeListener(@NotNull Lobby lobby) {
        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onChange(@NotNull FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}