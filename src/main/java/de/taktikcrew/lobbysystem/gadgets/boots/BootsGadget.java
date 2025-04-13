package de.taktikcrew.lobbysystem.gadgets.boots;

import de.smoofy.core.api.builder.ItemBuilder;
import de.taktikcrew.lobbysystem.gadgets.AbstractGadget;
import de.taktikcrew.lobbysystem.gadgets.meta.GadgetType;
import de.taktikcrew.lobbysystem.lobbyplayer.LobbyPlayer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;

@Getter
@Accessors(fluent = true)
public abstract class BootsGadget extends AbstractGadget {

    private final Color color;
    private final Particle particle;

    public BootsGadget(String requiredPermission, Color color, Particle particle, boolean active) {
        super(requiredPermission, GadgetType.BOOTS, Material.LEATHER_BOOTS, active);
        this.color = color;
        this.particle = particle;
    }

    @Override
    public ItemBuilder icon() {
        return super.icon().color(this.color);
    }

    @Override
    public void activate(LobbyPlayer lobbyPlayer) {
        lobbyPlayer.player().ifPresent(player -> player.getInventory().setBoots(this.icon().build()));
    }

    @Override
    public void deactivate(LobbyPlayer lobbyPlayer) {
        lobbyPlayer.player().ifPresent(player -> player.getInventory().setBoots(null));
    }

    public void spawnEffect(LobbyPlayer lobbyPlayer) {
        lobbyPlayer.player().ifPresent(player -> {
            var location = player.getLocation().clone().add(0, 0.1, 0);
            location.getWorld().spawnParticle(this.particle, location, 10, 0, 0.01, 0, 0.05);
        });
    }

    public void handleSneak(LobbyPlayer lobbyPlayer) {}
}
