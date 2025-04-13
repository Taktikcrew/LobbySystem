package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

// todo: particle
public class SpeedBoots extends BootsGadget {

    public SpeedBoots(boolean active) {
        super("boots.speed", Color.AQUA, Particle.HEART, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.DIVINE;
    }
}
