package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

public class AngryBoots extends BootsGadget {

    public AngryBoots(boolean active) {
        super("boots.angry", Color.RED, Particle.ANGRY_VILLAGER, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.RARE;
    }
}
