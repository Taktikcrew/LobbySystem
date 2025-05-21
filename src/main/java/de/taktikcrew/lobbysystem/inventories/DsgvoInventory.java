package de.taktikcrew.lobbysystem.inventories;

import de.smoofy.core.api.Core;
import de.smoofy.core.api.builder.InventoryBuilder;
import de.smoofy.core.api.builder.ItemBuilder;
import de.taktikcrew.lobbysystem.lobbyplayer.LobbyPlayerDAO;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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

    private final LobbyPlayerDAO lobbyPlayerDAO;

    private final Inventory inventory;

    public DsgvoInventory(@NotNull InventoryProvider inventoryProvider) {
        this.lobbyPlayerDAO = inventoryProvider.lobby().databaseProvider().lobbyPlayerDAO();

        this.inventory = InventoryBuilder.of(new Holder(), Component.translatable("dsgvo.menu.title"), 3)
                
                .fill(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).noName())

                .set(ItemBuilder.of(Material.GREEN_TERRACOTTA)
                        .name(Component.translatable("lobby.menu.dsgvo.item.accept.name", NamedTextColor.GREEN, TextDecoration.BOLD))
                        .event("dsgvo.accept", InventoryClickEvent.class, event -> {
                            if (!(event.getWhoClicked() instanceof Player player)) {
                                return;
                            }
                            this.lobbyPlayerDAO.get(player.getUniqueId()).ifPresent(lobbyPlayer ->
                                    lobbyPlayer.dsgvoAccepted(true));

                            player.closeInventory();
                            player.getActivePotionEffects().forEach(potionEffect ->
                                    player.removePotionEffect(potionEffect.getType()));

                            var corePlayer = Core.instance().corePlayerProvider().corePlayer(player);
                            inventoryProvider.lobbyPlayerInventory().setLobbyInventory(corePlayer, false);
                        }), 11)

                .set(ItemBuilder.of(Material.BOOK)
                        .name(Component.translatable("lobby.menu.dsgvo.item.dsgvo.name")
                                .color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))

                        .lore(Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.1"),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.2"),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.3"),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.4"),
                                Component.empty(),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.5"),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.6"),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.7"),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.8"),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.9"),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.10"),
                                Component.empty(),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.11"),
                                Component.translatable("lobby.menu.dsgvo.item.dsgvo.lore.12")
                        ), 13)

                .set(ItemBuilder.of(Material.RED_TERRACOTTA)
                        .name(Component.translatable("lobby.menu.dsgvo.item.reject.name", NamedTextColor.RED, TextDecoration.BOLD))
                        .event("dsgvo.reject", InventoryClickEvent.class, event -> {
                            if (!(event.getWhoClicked() instanceof Player player)) {
                                return;
                            }
                            player.kick(Component.translatable("dsgvo.rejected"));
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
