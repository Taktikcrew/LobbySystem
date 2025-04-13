package de.taktikcrew.lobbysystem.gadgets;

import de.smoofy.core.api.builder.ItemBuilder;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.gadgets.meta.GadgetRarity;
import de.taktikcrew.lobbysystem.gadgets.meta.GadgetType;
import de.taktikcrew.lobbysystem.lobbyplayer.LobbyPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class AbstractGadget {

    private final String name = getClass().getSimpleName();
    private final String requiredPermission;
    private final GadgetType type;
    private final Material icon;
    @Setter
    private boolean active;

    public abstract GadgetRarity rarity();

    public abstract void activate(LobbyPlayer lobbyPlayer);

    public abstract void deactivate(LobbyPlayer lobbyPlayer);

    public ItemBuilder icon() {
        return ItemBuilder.of(this.icon)
                .name(Component.translatable(this.name(), this.rarity().color()))
                .lore(Component.translatable("lobby.menu.player.item.gadgets.lore.category")
                                .arguments(Component.translatable(this.type().name(), NamedTextColor.WHITE)),
                        Component.translatable("lobby.menu.player.item.gadgets.lore.rarity")
                                .arguments(Component.translatable(this.rarity().key(), this.rarity().color())));
    }

    public boolean canUse(ICorePlayer corePlayer) {
        return corePlayer.bukkitPlayer().map(player -> player.hasPermission("lobby.gadgets." +
                this.requiredPermission)).orElse(false);
    }
}
