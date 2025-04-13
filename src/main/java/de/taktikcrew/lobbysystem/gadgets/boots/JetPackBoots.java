package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

//todo
public class JetPackBoots extends BootsGadget {

    public JetPackBoots(boolean active) {
        super("boots.jetpack", Color.RED, Particle.DUST, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.DIVINE;
    }
}
