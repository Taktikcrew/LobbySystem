package de.taktikcrew.lobbysystem.minigames.connection.tictactoe;

import de.taktikcrew.lobbysystem.minigames.connection.ConnectionGameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TicTacToeListener extends ConnectionGameListener implements Listener {

    public TicTacToeListener(TicTacToeManager ticTacToeManager) {
        super(ticTacToeManager);

        ticTacToeManager.lobby().getServer().getPluginManager().registerEvents(this, ticTacToeManager.lobby());
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
