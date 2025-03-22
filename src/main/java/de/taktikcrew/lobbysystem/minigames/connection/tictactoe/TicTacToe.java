package de.taktikcrew.lobbysystem.minigames.connection.tictactoe;

import com.google.common.collect.Lists;
import de.smoofy.core.api.builder.InventoryBuilder;
import de.smoofy.core.api.builder.ItemBuilder;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.minigames.AbstractGameManager;
import de.taktikcrew.lobbysystem.minigames.connection.ConnectionGame;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TicTacToe extends ConnectionGame {

    private final Random random = new Random();

    protected TicTacToe(AbstractGameManager<?> abstractGameManager, List<ICorePlayer> players, boolean botMatch) {
        super(abstractGameManager, Type.TIC_TAC_TOE, players, botMatch);
    }

    @Override
    protected void place(ICorePlayer corePlayer, int slot) {
        var item = ItemBuilder.of(this.skin(corePlayer)).name(corePlayer.displayName()).build();
        for (var inventory : this.players().values()) {
            inventory.setItem(slot, item);
            this.changeGlass(null, inventory);
        }

        var inventories = this.players().values().toArray(new Inventory[]{});
        var inventory = inventories[0];
        if (!checkWon(inventory, item.getType(), slot)) {
            this.changeTurn();
        }
    }

    @Override
    protected boolean isPlaceable(int slot) {
        return Set.of(12, 13, 14, 21, 22, 23, 30, 31, 32).contains(slot);
    }

    @Override
    protected int botMove() {
        var slots = new int[]{12, 13, 14, 21, 22, 23, 30, 31, 32};
        var slot = slots[this.random.nextInt(9)];
        while (this.players().get(this.turn()).getItem(slot) != null) {
            slot = slots[this.random.nextInt(9)];
        }
        return slot;
    }

    private List<Integer> checkLine(Inventory inventory, Material skin, int... slots) {
        List<Integer> validSlots = Lists.newArrayList();
        for (var slot : slots) {
            var item = inventory.getItem(slot);
            if (item == null || !item.getType().equals(skin)) {
                break;
            }
            validSlots.add(slot);
        }
        return validSlots;
    }

    @Override
    protected List<Integer> checkHorizontal(Inventory inventory, Material skin, int slot) {
        if (slot == 12 || slot == 13 || slot == 14) {
            return this.checkLine(inventory, skin, 12, 13, 14);
        } else if (slot == 21 || slot == 22 || slot == 23) {
            return this.checkLine(inventory, skin, 21, 22, 23);
        } else if (slot == 30 || slot == 31 || slot == 32) {
            return this.checkLine(inventory, skin, 30, 31, 32);
        }
        return Lists.newArrayList();
    }

    @Override
    protected List<Integer> checkVertical(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        if (slot == 12 || slot == 21 || slot == 30) {
            for (int i : new int[]{12, 21, 30}) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            }
        } else if (slot == 13 || slot == 22 || slot == 31) {
            for (int i : new int[]{13, 22, 31}) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            }
        } else if (slot == 14 || slot == 23 || slot == 32) {
            for (int i : new int[]{14, 23, 32}) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            }
        }
        return slots;
    }

    @Override
    protected List<Integer> checkDiagonalLeftToRight(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        for (int i : new int[]{12, 22, 32}) {
            var item = inventory.getItem(i);
            if (item == null || !item.getType().equals(skin)) {
                break;
            }
            slots.add(i);
        }
        return slots;
    }

    @Override
    protected List<Integer> checkDiagonalRightToLeft(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        for (int i : new int[]{14, 22, 30}) {
            var item = inventory.getItem(i);
            if (item == null || !item.getType().equals(skin)) {
                break;
            }
            slots.add(i);
        }
        return slots;
    }

    @Override
    protected void changeExit(ICorePlayer corePlayer, Inventory inventory) {
        inventory.setItem(44, ItemBuilder.of(Material.FIREWORK_ROCKET)
                .name(Component.translatable("lobby.minigame.item.rematch.name"))
                .lore(Component.translatable("lobby.minigame.item.rematch.lore"))
                .build());
    }

    @Override
    protected void changeGlass(@Nullable ICorePlayer corePlayer, Inventory inventory) {
        var gray = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).noName().build();
        var turn = ItemBuilder.of(Material.YELLOW_STAINED_GLASS_PANE).name(Component.translatable("lobby.minigame.item.turn.name")).build();
        var lose = ItemBuilder.of(Material.RED_STAINED_GLASS_PANE).name(Component.translatable("lobby.minigame.item.lose.name")).build();
        var win = ItemBuilder.of(Material.LIME_STAINED_GLASS_PANE).name(Component.translatable("lobby.minigame.item.win.name")).build();

        if (corePlayer != null) {
            if (this.finished()) {
                if (!corePlayer.equals(this.turn())) {
                    for (var slot : new int[]{0, 9}) {
                        inventory.setItem(slot, lose);
                    }
                    for (var slot : new int[]{8, 17}) {
                        inventory.setItem(slot, win);
                    }
                } else {
                    for (var slot : new int[]{0, 9}) {
                        inventory.setItem(slot, win);
                    }
                    for (var slot : new int[]{8, 17}) {
                        inventory.setItem(slot, lose);
                    }
                }
                this.changeExit(corePlayer, inventory);
            } else {
                if (!corePlayer.equals(this.turn())) {
                    for (var slot : new int[]{0, 9}) {
                        inventory.setItem(slot, gray);
                    }
                    for (var slot : new int[]{8, 17}) {
                        inventory.setItem(slot, turn);
                    }
                } else {
                    corePlayer.bukkitPlayer().ifPresent(player ->
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1));

                    for (var slot : new int[]{0, 9}) {
                        inventory.setItem(slot, turn);
                    }
                    for (var slot : new int[]{8, 17}) {
                        inventory.setItem(slot, gray);
                    }
                }
            }
        }
    }

    @Override
    protected void changeTime() {
        for (var inventory : this.players().values()) {
            inventory.setItem(36, ItemBuilder.of(Material.CLOCK)
                    .amount(this.time())
                    .name(Component.translatable("lobby.minigame.item.time.name"))
                    .lore(Component.translatable("lobby.minigame.item.time.lore"))
                    .build());
        }
    }

    @Override
    protected Inventory createInventory(ICorePlayer corePlayer, ICorePlayer opponent) {
        return InventoryBuilder.of(Component.translatable("minigame.menu.game.title")
                        .arguments(this.type().gameName(), opponent.displayName()), 6)

                .fill(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).noName())
                .clear(12, 13, 14, 21, 22, 23, 30, 31, 32)
                .set(ItemBuilder.of(Material.PLAYER_HEAD).skullOwner(corePlayer).name(corePlayer.displayName()), 18)
                .set(ItemBuilder.of(this.skin(corePlayer)).name(Component.translatable("lobby.minigame.item.skin.name")), 27)
                .set(ItemBuilder.of(Material.PLAYER_HEAD).skullOwner(opponent).name(opponent.displayName()), 26)
                .set(ItemBuilder.of(this.skin(opponent)).name(Component.translatable("lobby.minigame.item.skin.name")), 35)
                .set(ItemBuilder.of(Material.SPRUCE_DOOR).name(Component.translatable("lobby.minigame.item.quit.name")).lore(), 53)
                .build();
    }
}
