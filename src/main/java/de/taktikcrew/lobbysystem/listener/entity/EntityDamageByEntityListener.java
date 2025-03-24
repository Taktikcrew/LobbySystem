package de.taktikcrew.lobbysystem.listener.entity;

import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class EntityDamageByEntityListener implements Listener {

    public EntityDamageByEntityListener(@NotNull Lobby lobby) {
        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onDamage(@NotNull EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player damager)) {
            return;
        }
        event.setCancelled(!damager.getGameMode().equals(GameMode.CREATIVE));
    }
}