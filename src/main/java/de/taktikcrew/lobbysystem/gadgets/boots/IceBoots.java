package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

// todo: particle
public class IceBoots extends BootsGadget {

    public IceBoots(boolean active) {
        super("boots.ice", Color.AQUA, Particle.DRIPPING_WATER, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.RARE;
    }
}
