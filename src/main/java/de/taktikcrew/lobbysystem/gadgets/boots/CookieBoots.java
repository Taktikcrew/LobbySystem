package de.taktikcrew.lobbysystem.gadgets.boots;

import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import org.bukkit.Color;
import org.bukkit.Particle;

// todo
public class CookieBoots extends BootsGadget {

    public CookieBoots(boolean active) {
        super("boots.cookie", Color.MAROON, Particle.HEART, active);
    }

    @Override
    public GadgetRarity rarity() {
        return GadgetRarity.MYTHIC;
    }
}
