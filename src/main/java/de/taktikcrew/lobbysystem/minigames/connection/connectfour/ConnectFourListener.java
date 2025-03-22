package de.taktikcrew.lobbysystem.minigames.connection.connectfour;

import de.taktikcrew.lobbysystem.minigames.connection.ConnectionGameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectFourListener extends ConnectionGameListener implements Listener {

    public ConnectFourListener(ConnectFourManager connectFourManager) {
        super(connectFourManager);

        connectFourManager.lobby().getServer().getPluginManager().registerEvents(this, connectFourManager.lobby());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        this.handleInventoryClickEvent(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        this.handleInventoryCloseEvent(event);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.handlePlayerQuitEvent(event);
    }
}
