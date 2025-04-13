package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

public class MusicBoots extends BootsGadget {

    public MusicBoots(boolean active) {
        super("boots.music", Color.OLIVE, Particle.NOTE, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.RARE;
    }
}
