package de.taktikcrew.lobbysystem.inventories;

import de.smoofy.core.api.builder.InventoryBuilder;
import de.smoofy.core.api.builder.ItemBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class NavigationInventory {

    private final Inventory inventory;

    public NavigationInventory() {
        this.inventory = InventoryBuilder.of(new Holder(), Component.translatable("navigator.menu.title"), 4)

                .fill(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).noName())
                .set(ItemBuilder.of(Material.BLUE_STAINED_GLASS_PANE).noName(), 0, 8, 27, 35)
                .set(ItemBuilder.of(Material.CYAN_STAINED_GLASS_PANE).noName(), 1, 2, 3, 4, 5, 6, 7, 9, 17, 18, 26, 28, 29, 30, 31, 32, 33, 34)

                .set(ItemBuilder.of(Material.DIAMOND)
                        .name(Component.translatable("lobby.menu.navigator.item.daily_reward.name"))
                        .event("navigator.daily_reward", InventoryClickEvent.class, event -> {

                        }), 11)

                .set(ItemBuilder.of(Material.MAGMA_CREAM)
                        .name(Component.translatable("lobby.menu.navigator.item.spawn.name"))
                        .event("navigator.spawn", InventoryClickEvent.class, event -> {

                        }), 13)

                .set(ItemBuilder.of(Material.TURTLE_HELMET)
                        .name(Component.translatable("lobby.menu.navigator.item.event_server.name"))
                        .event("navigator.event_server", InventoryClickEvent.class, event -> {

                        }), 15)

                .set(ItemBuilder.of(Material.FILLED_MAP)
                        .name(Component.translatable("lobby.menu.navigator.item.bingo.name"))
                        .event("navigator.bingo", InventoryClickEvent.class, event -> {

                        }), 19)

                .set(ItemBuilder.of(Material.ANVIL)
                        .name(Component.translatable("lobby.menu.navigator.item.challenge.name"))
                        .event("navigator.challenge", InventoryClickEvent.class, event -> {

                        }), 21)

                .set(ItemBuilder.of(Material.TOTEM_OF_UNDYING)
                        .name(Component.translatable("lobby.menu.navigator.item.manhunt.name"))
                        .event("navigator.manhunt", InventoryClickEvent.class, event -> {

                        }), 23)

                .set(ItemBuilder.of(Material.PLAYER_HEAD)
                        .name(Component.translatable("lobby.menu.navigator.item.smp.name"))
                        .event("navigator.smp", InventoryClickEvent.class, event -> {

                        }), 25)

                .build();
    }

    public static class Holder implements InventoryHolder {

        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(null, 9);
        }
    }
}
