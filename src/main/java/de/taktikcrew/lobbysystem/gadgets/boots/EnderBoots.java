package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

// todo
public class EnderBoots extends BootsGadget {

    public EnderBoots(boolean active) {
        super("boots.ender", Color.PURPLE, Particle.END_ROD, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.EPIC;
    }
}
