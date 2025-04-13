package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

// todo: particle
public class YoloBoots extends BootsGadget {

    public YoloBoots(boolean active) {
        super("boots.yolo", Color.MAROON, Particle.HEART, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.RARE;
    }
}
