package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

public class BoneBoots extends BootsGadget {

    public BoneBoots(boolean active) {
        super("boots.bone", Color.GRAY, Particle.DUST, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.MYTHIC;
    }
}
