package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

// todo: particle
public class SuperJumpBoots extends BootsGadget {

    public SuperJumpBoots(boolean active) {
        super("boots.superjump", Color.GREEN, Particle.HEART, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.DIVINE;
    }
}
