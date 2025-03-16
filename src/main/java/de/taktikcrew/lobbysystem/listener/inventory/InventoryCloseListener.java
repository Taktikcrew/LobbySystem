package de.taktikcrew.lobbysystem.listener.inventory;

import de.smoofy.core.api.Core;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.inventories.DsgvoInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    private final Lobby lobby;

    public InventoryCloseListener(Lobby lobby) {
        this.lobby = lobby;

        this.lobby.getServer().getPluginManager().registerEvents(this, this.lobby);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        var player = event.getPlayer();
        if (event.getReason().equals(InventoryCloseEvent.Reason.PLUGIN)) {
            return;
        }
        if (event.getInventory().getHolder() == null) {
            return;
        }
        if (event.getInventory().getHolder() instanceof DsgvoInventory.Holder) {
            Core.instance().coreTask().later(() -> player.openInventory(this.lobby.dsgvoInventory().inventory()), 1);
        }
    }
}
