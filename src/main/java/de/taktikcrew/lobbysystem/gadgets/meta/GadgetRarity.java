package de.taktikcrew.lobbysystem.gadgets.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum GadgetRarity {

    COMMON("", NamedTextColor.WHITE),
    UNCOMMON("", NamedTextColor.GREEN),
    RARE("", NamedTextColor.BLUE),
    EPIC("", NamedTextColor.DARK_PURPLE),
    LEGENDARY("", NamedTextColor.GOLD),
    MYTHIC("", NamedTextColor.RED),
    DIVINE("", NamedTextColor.DARK_RED);

    private final String key;
    private final NamedTextColor color;
}
