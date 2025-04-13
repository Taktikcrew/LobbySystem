package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

public class RainyBoots extends BootsGadget {

    public RainyBoots(boolean active) {
        super("boots.rainy", Color.BLUE, Particle.RAIN, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.RARE;
    }
}
