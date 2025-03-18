package de.taktikcrew.lobbysystem.inventories;

import de.smoofy.core.api.builder.InventoryBuilder;
import de.smoofy.core.api.builder.ItemBuilder;
import de.taktikcrew.lobbysystem.database.LobbyPlayerDAO;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class DsgvoInventory {

    private final InventoryProvider inventoryProvider;

    private final LobbyPlayerDAO lobbyPlayerDAO;

    private final Inventory inventory;

    public DsgvoInventory(InventoryProvider inventoryProvider) {
        this.inventoryProvider = inventoryProvider;
        this.lobbyPlayerDAO = this.inventoryProvider.lobby().lobbyPlayerDAO();

        this.inventory = InventoryBuilder.of(new Holder(), MiniMessage.miniMessage().deserialize("<red><bold>DSGVO"), 3)
                .fill(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).noName())

                .set(ItemBuilder.of(Material.GREEN_TERRACOTTA)
                        .name(Component.translatable("inv-dsgvo.item.dsgvo.accept", NamedTextColor.GREEN, TextDecoration.BOLD))
                        .event("inv-dsgvo.item.dsgvo.accept", InventoryClickEvent.class, event -> {
                            if (!(event.getWhoClicked() instanceof Player player)) {
                                return;
                            }
                            this.lobbyPlayerDAO.get(player.getUniqueId()).ifPresent(lobbyPlayer ->
                                    lobbyPlayer.dsgvoAccepted(true));

                            player.closeInventory();
                            player.getActivePotionEffects().forEach(potionEffect ->
                                    player.removePotionEffect(potionEffect.getType()));
                        }), 11)

                .set(ItemBuilder.of(Material.BOOK)
                        .name(MiniMessage.miniMessage().deserialize("<yellow><bold>DSGVO"))
                        .lore(Component.translatable("inv-dsgvo.item.dsgvo.lore.1"),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.2"),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.3"),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.4"),
                                Component.empty(),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.5"),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.6"),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.7"),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.8"),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.9"),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.10"),
                                Component.empty(),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.11"),
                                Component.translatable("inv-dsgvo.item.dsgvo.lore.12")
                        ), 13)

                .set(ItemBuilder.of(Material.RED_TERRACOTTA)
                        .name(Component.translatable("inv-dsgvo.item.dsgvo.reject", NamedTextColor.RED, TextDecoration.BOLD))
                        .event("inv-dsgvo.item.dsgvo.reject", InventoryClickEvent.class, event -> {
                            if (!(event.getWhoClicked() instanceof Player player)) {
                                return;
                            }
                            player.kick(Component.text("KICKED"));
                        }), 15)

                .build();
    }

    public static class Holder implements InventoryHolder {

        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(null, 9);
        }
    }
}
