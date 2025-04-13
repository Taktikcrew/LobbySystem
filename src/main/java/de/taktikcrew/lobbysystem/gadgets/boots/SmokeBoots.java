package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

public class SmokeBoots extends BootsGadget {

    public SmokeBoots(boolean active) {
        super("boots.smoke", Color.GRAY, Particle.SMOKE, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.RARE;
    }
}
