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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TicTacToe extends ConnectionGame {

    public TicTacToe(AbstractGameManager<?> abstractGameManager, List<ICorePlayer> players, boolean botMatch) {
        super(abstractGameManager, Type.TIC_TAC_TOE, players, botMatch);
    }

    @Override
    public void place(ICorePlayer corePlayer, int slot) {
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
    public boolean isPlaceable(int slot) {
        var placeableSlots = new int[]{12, 13, 14, 21, 22, 23, 30, 31, 32};
        return Arrays.binarySearch(placeableSlots, slot) >= 0;
    }

    @Override
    public List<Integer> checkHorizontal(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        if (slot == 12 || slot == 13 || slot == 14) {
            for (int i : new int[]{12, 13, 14}) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            }
        } else if (slot == 21 || slot == 22 || slot == 23) {
            for (int i : new int[]{21, 22, 23}) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            }
        } else if (slot == 30 || slot == 31 || slot == 32) {
            for (int i : new int[]{30, 31, 32}) {
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
    public List<Integer> checkVertical(Inventory inventory, Material skin, int slot) {
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
    public List<Integer> checkDiagonalLeftToRight(Inventory inventory, Material skin, int slot) {
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
    public List<Integer> checkDiagonalRightToLeft(Inventory inventory, Material skin, int slot) {
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
    public void changeExit(ICorePlayer corePlayer, Inventory inventory) {
        inventory.setItem(44, ItemBuilder.of(Material.FIREWORK_ROCKET)
                .name(Component.translatable("lobby.minigame.item.rematch.name"))
                .lore(Component.translatable("lobby.minigame.item.rematch.lore"))
                .build());
    }

    @Override
    public void changeGlass(ICorePlayer corePlayer, Inventory inventory) {
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
    public void changeTurn() {
        this.turn(this.otherPlayer(this.turn()));
        for (var corePlayer : this.players().keySet()) {
            var inventory = this.players().get(corePlayer);
            if (inventory.firstEmpty() != -1) {
                this.changeGlass(corePlayer, inventory);
            } else {
                this.draw();
                return;
            }
        }
        if (this.botMatch() && this.turn().name().equals("Bot")) {
            int[] slots = new int[]{12, 13, 14, 21, 22, 23, 30, 31, 32};
            var random = new Random();
            var slot = slots[random.nextInt(9)];
            while (this.players().get(this.turn()).getItem(slot) != null) {
                slot = slots[random.nextInt(9)];
            }
            this.place(this.turn(), slot);
        }
        this.schedule();
    }

    @Override
    public void changeTime() {
        for (var inventory : this.players().values()) {
            inventory.setItem(36, ItemBuilder.of(Material.CLOCK)
                    .amount(this.time())
                    .name(Component.translatable("lobby.minigame.item.time.name"))
                    .lore(Component.translatable("lobby.minigame.item.time.lore"))
                    .build());
        }
    }

    @Override
    public Inventory createInventory(ICorePlayer corePlayer, ICorePlayer opponent) {
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
