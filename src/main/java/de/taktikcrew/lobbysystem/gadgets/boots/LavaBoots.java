package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

public class LavaBoots extends BootsGadget {

    public LavaBoots(boolean active) {
        super("boots.lava", Color.ORANGE, Particle.LAVA, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.RARE;
    }
}
