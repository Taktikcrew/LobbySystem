package de.taktikcrew.lobbysystem.minigames.connection;

import de.smoofy.core.api.Core;
import de.smoofy.core.api.builder.ItemBuilder;
import de.taktikcrew.lobbysystem.minigames.AbstractGameManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;

public class ConnectionGameListener {

    private final AbstractGameManager<?> gameManager;

    private final Component prefix;

    public ConnectionGameListener(AbstractGameManager<?> gameManager) {
        this.gameManager = gameManager;

        this.prefix = this.gameManager.prefix();
    }

    public void handleInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }

        var corePlayer = Core.instance().corePlayerProvider().corePlayer(player);
        var connectionGame = this.gameManager.game(corePlayer);

        if (connectionGame == null) {
            return;
        }
        if (!event.getClickedInventory().equals(connectionGame.players().get(corePlayer))) {
            return;
        }

        event.setCancelled(true);

        var item = event.getCurrentItem();

        if (!connectionGame.started()) {
            if (!connectionGame.playerSkin().containsKey(corePlayer)) {
                if (event.getSlot() % 9 > 1 && event.getSlot() % 9 < 7) {
                    if (item == null) {
                        return;
                    }
                    if (item.getType().equals(Material.BARRIER) || item.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 1);
                        return;
                    }
                    connectionGame.selectSkin(corePlayer, event.getSlot(), item.getType());
                }
            }
            return;
        }
        if (item != null) {
            if (item.getType().equals(Material.FIREWORK_ROCKET)) {
                if (connectionGame.finished()) {
                    if (this.gameManager.game(connectionGame.otherPlayer(corePlayer)) != null) {
                        if (!corePlayer.equals(connectionGame.rematch())) {
                            if (connectionGame.rematch() == null) {
                                if (connectionGame.botMatch()) {
                                    if (connectionGame.bukkitTask() != null) {
                                        connectionGame.bukkitTask().cancel();
                                    }
                                    this.gameManager.removeGame(corePlayer);
                                    this.gameManager.createBotGame(corePlayer);
                                } else {
                                    connectionGame.rematch(corePlayer);
                                    connectionGame.createRematchInventories();
                                }
                            } else {
                                var rematchPlayer = connectionGame.rematch();
                                connectionGame.rematch(null);
                                if (connectionGame.bukkitTask() != null) {
                                    connectionGame.bukkitTask().cancel();
                                }
                                this.gameManager.removeGame(rematchPlayer, corePlayer);
                                this.gameManager.createGame(rematchPlayer, corePlayer);
                            }
                        }
                    } else {
                        corePlayer.message(this.prefix.append(Component.translatable("minigame.message.opponent_already_left")));
                        player.closeInventory();
                    }
                    return;
                }
            }
            if (!item.getType().equals(Material.SPRUCE_DOOR)) {
                return;
            }
            if (!item.getItemMeta().hasEnchant(Enchantment.INFINITY)) {
                event.getInventory().setItem(event.getSlot(), ItemBuilder.of(Material.SPRUCE_DOOR)
                        .name(Component.translatable("lobby.minigame.item.quit_confirm.name"))
                        .lore(Component.translatable("lobby.minigame.item.quit_confirm.lore"))
                        .enchantUnsafe(1, Enchantment.INFINITY)
                        .itemFlags(ItemFlag.HIDE_ENCHANTS)
                        .build());
                return;
            }

            connectionGame.finished(true);
            if (connectionGame.bukkitTask() != null) {
                connectionGame.bukkitTask().cancel();
            }

            corePlayer.message(this.prefix.append(Component.translatable("minigame.message.give_up")));
            player.closeInventory();

            var otherPlayer = connectionGame.otherPlayer(corePlayer);
            otherPlayer.message(this.prefix.append(Component.translatable("minigame.message.opponent_give_up")
                    .arguments(corePlayer.displayName())));

            this.gameManager.removeGame(corePlayer, otherPlayer);
            otherPlayer.bukkitPlayer().ifPresent(HumanEntity::closeInventory);

            return;
        }

        if (connectionGame.finished()) {
            return;
        }
        if (!corePlayer.equals(connectionGame.turn()) || connectionGame.animation()) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 1);
            return;
        }
        if (!connectionGame.isPlaceable(event.getSlot())) {
            return;
        }
        connectionGame.place(corePlayer, connectionGame.type().animated() ? event.getSlot() % 9 : event.getSlot());
    }

    public void handleInventoryCloseEvent(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        var corePlayer = Core.instance().corePlayerProvider().corePlayer(player);
        var connectionGame = this.gameManager.game(corePlayer);

        if (connectionGame == null) {
            return;
        }
        if (!event.getInventory().equals(connectionGame.players().get(corePlayer))) {
            return;
        }
        if (connectionGame.rematch() != null) {
            var otherPlayer = connectionGame.otherPlayer(corePlayer);

            this.gameManager.removeGame(corePlayer, otherPlayer);
            corePlayer.message(this.prefix.append(connectionGame.rematch().equals(corePlayer) ?
                    Component.translatable("minigame.message.rematch_revoke") :
                    Component.translatable("minigame.message.rematch_reject")));

            otherPlayer.message(this.prefix.append(connectionGame.rematch().equals(corePlayer) ?
                    Component.translatable("minigame.message.opponent_revoked_rematch") :
                    Component.translatable("minigame.message.opponent_rejected_rematch")));
            otherPlayer.bukkitPlayer().ifPresent(HumanEntity::closeInventory);

            return;
        }

        if (connectionGame.finished()) {
            return;
        }

        Core.instance().coreTask().later(() -> player.openInventory(connectionGame.players().get(corePlayer)), 1);

        corePlayer.message(this.prefix.append(Component.translatable("minigame.message.use_door")));
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 1);
    }

    public void handlePlayerQuitEvent(PlayerQuitEvent event) {
        var corePlayer = Core.instance().corePlayerProvider().corePlayer(event.getPlayer());
        var connectionGame = this.gameManager.game(corePlayer);

        if (connectionGame == null) {
            return;
        }

        connectionGame.finished(true);

        var otherPlayer = connectionGame.otherPlayer(corePlayer);
        otherPlayer.message(this.prefix.append(Component.translatable("minigame.message.opponent_left")
                .arguments(corePlayer.displayName())));
        otherPlayer.bukkitPlayer().ifPresent(HumanEntity::closeInventory);

        this.gameManager.removeGame(corePlayer, otherPlayer);
    }

}
