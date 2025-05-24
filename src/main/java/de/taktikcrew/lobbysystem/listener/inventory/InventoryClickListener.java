package de.taktikcrew.lobbysystem.listener.inventory;

import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryClickListener implements Listener {

    public InventoryClickListener(Lobby lobby) {
        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
    }

}
