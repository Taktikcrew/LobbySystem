package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

// todo
public class BunnyBoots extends BootsGadget {

    public BunnyBoots(boolean active) {
        super("boots.bunny", Color.MAROON, Particle.HEART, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.DIVINE;
    }
}
