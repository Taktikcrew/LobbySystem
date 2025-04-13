package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

//todo: particle
public class GhostBoots extends BootsGadget {

    public GhostBoots(boolean active) {
        super("boots.ghost", Color.GRAY, Particle.DUST, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.RARE;
    }
}
