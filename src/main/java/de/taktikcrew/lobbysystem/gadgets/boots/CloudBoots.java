package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

// todo
public class CloudBoots extends BootsGadget {

    public CloudBoots(boolean active) {
        super("boots.cloud", Color.AQUA, Particle.CLOUD, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.MYTHIC;
    }
}
