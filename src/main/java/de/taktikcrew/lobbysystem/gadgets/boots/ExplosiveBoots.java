package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

public class ExplosiveBoots extends BootsGadget {

    public ExplosiveBoots(boolean active) {
        super("boots.explosive", Color.GRAY, Particle.EXPLOSION, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.EPIC;
    }
}
