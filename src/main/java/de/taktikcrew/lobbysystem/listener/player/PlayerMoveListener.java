package de.taktikcrew.lobbysystem.listener.player;

import com.google.common.collect.Maps;
import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private final Map<UUID, Location> backTeleportLocation = Maps.newHashMap();

    public PlayerMoveListener(@NotNull Lobby lobby) {
        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onMove(@NotNull PlayerMoveEvent event) {
        var player = event.getPlayer();
        if (player.getLocation().getBlock().isSolid()) {
            this.backTeleportLocation.put(player.getUniqueId(), event.getFrom());
            return;
        }
        if (player.getFallDistance() > 10) {
            player.setFallDistance(0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 2, false, false, false));
            player.teleport(this.backTeleportLocation.getOrDefault(player.getUniqueId(), player.getWorld().getSpawnLocation()));
        }
    }
}