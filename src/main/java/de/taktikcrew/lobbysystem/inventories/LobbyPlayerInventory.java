package de.taktikcrew.lobbysystem.inventories;

import de.smoofy.core.api.builder.ItemBuilder;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.database.LobbyPlayerDAO;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class LobbyPlayerInventory {

    private final LobbyPlayerDAO lobbyPlayerDAO;

    public LobbyPlayerInventory(@NotNull InventoryProvider inventoryProvider) {
        this.lobbyPlayerDAO = inventoryProvider.lobby().databaseProvider().lobbyPlayerDAO();
    }

    public void setLobbyInventory(@NotNull ICorePlayer corePlayer) {
        corePlayer.inventory().clear();

        corePlayer.inventory().setItem(0, ItemBuilder.of(Material.COMPASS)
                .name(Component.translatable("lobby.menu.player.item.navigator.name"))
                .namespacedKey(InventoryItemKeys.NAVIGATOR.key(), PersistentDataType.BOOLEAN, true)
                .build());

        corePlayer.inventory().setItem(1, ItemBuilder.of(Material.FIREWORK_STAR)
                .name(Component.translatable("lobby.menu.player.item.player_hider.name"))
                .namespacedKey(InventoryItemKeys.PLAYER_HIDER.key(), PersistentDataType.BOOLEAN, true)
                .build());

        corePlayer.inventory().setItem(7, ItemBuilder.of(Material.CHEST)
                .name(Component.translatable("lobby.menu.player.item.gadgets.name"))
                .namespacedKey(InventoryItemKeys.GADGETS.key(), PersistentDataType.BOOLEAN, true)
                .build());

        corePlayer.inventory().setItem(8, ItemBuilder.of(Material.PLAYER_HEAD)
                .skullOwner(corePlayer)
                .name(Component.translatable("lobby.menu.player.item.profile.name"))
                .namespacedKey(InventoryItemKeys.PROFILE.key(), PersistentDataType.BOOLEAN, true)
                .build());

        corePlayer.bukkitPlayer().ifPresent(player -> {
            if (!player.hasPermission("lobby.nick")) {
                return;
            }

            corePlayer.inventory().setItem(3, ItemBuilder.of(Material.NAME_TAG)
                    .name(Component.translatable("lobby.menu.player.item.nick.name"))
                    .namespacedKey(InventoryItemKeys.NICK.key(), PersistentDataType.BOOLEAN, true)
                    .build());

            if (!player.hasPermission("lobby.vip")) {
                return;
            }

            corePlayer.inventory().setItem(5, ItemBuilder.of(Material.TNT)
                    .name(Component.translatable("lobby.menu.player.item.silent_hub.name"))
                    .namespacedKey(InventoryItemKeys.SILENT_HUB.key(), PersistentDataType.BOOLEAN, true)
                    .build());
        });
    }

    public void setJumpAndRunInventory(ICorePlayer corePlayer) {
        corePlayer.inventory().clear();

        corePlayer.inventory().setItem(0, ItemBuilder.of(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .name(Component.translatable("lobby.jar.item.checkpoint.name"))
                .namespacedKey(InventoryItemKeys.JAR_CHECKPOINT.key(), PersistentDataType.BOOLEAN, true)
                .build());

        corePlayer.inventory().setItem(8, ItemBuilder.of(Material.BARRIER)
                .name(Component.translatable("lobby.jar.item.abort.name"))
                .namespacedKey(InventoryItemKeys.JAR_ABORT.key(), PersistentDataType.BOOLEAN, true)
                .build());
    }
}
