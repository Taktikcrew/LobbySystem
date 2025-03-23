package de.taktikcrew.lobbysystem.listener.player;

import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    public PlayerInteractListener(Lobby lobby) {
        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // todo: implement
    }
}