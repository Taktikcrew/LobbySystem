package de.taktikcrew.lobbysystem.minigames.connection.connectfour;

import com.google.common.collect.Lists;
import de.smoofy.core.api.builder.ItemBuilder;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.minigames.AbstractGameManager;
import de.taktikcrew.lobbysystem.minigames.connection.ConnectionGame;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class ConnectFour extends ConnectionGame {

    private final Random random = new Random();

    private int lastSlot;

    protected ConnectFour(AbstractGameManager<?> abstractGameManager, List<ICorePlayer> players, boolean botMatch) {
        super(abstractGameManager, Type.CONNECT_FOUR, players, botMatch);
    }

    @Override
    protected void place(ICorePlayer corePlayer, int slot) {
        this.animation(true);

        var item = ItemBuilder.of(this.skin(corePlayer)).name(corePlayer.displayName()).build();
        this.lastSlot = slot;
        for (var inventory : this.players().values()) {
            inventory.setItem(this.lastSlot, item);
            this.changeGlass(null, inventory);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                var inventories = players().values().toArray(new Inventory[]{});
                var inventory = inventories[0];
                if (inventory.getSize() > lastSlot + 9 && inventory.getItem(lastSlot + 9) == null) {
                    if (inventory.getItem(lastSlot) != null) {
                        for (var inventory2 : inventories) {
                            inventory2.setItem(lastSlot, null);
                        }
                    }
                    for (var inventory2 : inventories) {
                        inventory2.setItem(lastSlot + 9, item);
                    }
                    lastSlot = lastSlot + 9;
                } else {
                    if (noWinnerExists(inventory, item.getType(), lastSlot)) {
                        animation(false);
                        changeTurn();
                    }
                    cancel();
                }
            }
        }.runTaskTimer(this.abstractGameManager().lobby(), 5, 5);
    }

    @Override
    protected boolean isPlaceable(int slot) {
        return slot % 9 > 0;
    }

    @Override
    protected int botMove() {
        var slot = this.random.nextInt(7) + 1;
        while (this.players().get(this.turn()).getItem(slot) != null) {
            slot = this.random.nextInt(7) + 1;
        }
        return slot;
    }

    @Override
    protected List<Integer> checkHorizontal(Inventory inventory, Material skin, int slot) {
        var y = slot / 9;
        List<Integer> slots = Lists.newArrayList();
        slots.add(slot);

        for (int i = (slot - 1); i > (slot - 5); i--) {
            if (i / 9 != y || i < 1) {
                break;
            }
            if (this.checkPattern(inventory, skin, slots, i)) {
                break;
            }
        }

        for (int i = (slot + 1); i < (slot + 5); i++) {
            if (i / 9 != y || i > 53) {
                break;
            }
            if (this.checkPattern(inventory, skin, slots, i)) {
                break;
            }
        }

        return slots;
    }

    @Override
    protected List<Integer> checkVertical(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        slots.add(slot);

        for (int i = (slot - 9); i > (slot - (5 * 9)); i -= 9) {
            if (i < 0) {
                break;
            }
            if (this.checkPattern(inventory, skin, slots, i)) {
                break;
            }
        }

        for (int i = (slot + 9); i < (slot + (5 * 9)); i += 9) {
            if (i > 53) {
                break;
            }
            if (this.checkPattern(inventory, skin, slots, i)) {
                break;
            }
        }

        return slots;
    }

    @Override
    protected List<Integer> checkDiagonalLeftToRight(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        slots.add(slot);

        for (int i = (slot - 8); i > (slot - (5 * 8)); i -= 8) {
            if (i < 0) {
                break;
            }
            if (this.checkPattern(inventory, skin, slots, i)) {
                break;
            }
        }

        for (int i = (slot + 8); i < (slot + (5 * 8)); i += 8) {
            if (i > 53) {
                break;
            }
            if (this.checkPattern(inventory, skin, slots, i)) {
                break;
            }
        }

        return slots;
    }

    @Override
    protected List<Integer> checkDiagonalRightToLeft(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        slots.add(slot);

        for (int i = (slot - 10); i > (slot - (5 * 10)); i -= 10) {
            if (i < 0) {
                break;
            }
            if (this.checkPattern(inventory, skin, slots, i)) {
                break;
            }
        }

        for (int i = (slot + 10); i < (slot + (5 * 10)); i += 10) {
            if (i > 53) {
                break;
            }
            if (this.checkPattern(inventory, skin, slots, i)) {
                break;
            }
        }

        return slots;
    }

    private boolean checkPattern(Inventory inventory, Material skin, List<Integer> slots, int i) {
        if (i % 9 > 0 && i % 9 < 8) {
            var item = inventory.getItem(i);
            if (item == null || !item.getType().equals(skin)) {
                return true;
            }
            slots.add(i);
        } else {
            return true;
        }
        return false;
    }

    @Override
    protected void changeExit(ICorePlayer corePlayer, @NotNull Inventory inventory) {
        inventory.setItem(53, ItemBuilder.of(Material.FIREWORK_ROCKET)
                .name(Component.translatable("lobby.minigame.item.rematch.name"))
                .lore(Component.translatable("lobby.minigame.item.rematch.lore"))
                .build());
    }

    @Override
    protected void changeGlass(ICorePlayer corePlayer, Inventory inventory) {
        if (this.finished()) {
            if (!corePlayer.equals(this.turn())) {
                for (var slot : new int[]{0, 9, 36}) {
                    inventory.setItem(slot, this.loseItem());
                }
                for (var slot : new int[]{8, 17, 44}) {
                    inventory.setItem(slot, this.winItem());
                }
            } else {
                for (var slot : new int[]{0, 9, 36}) {
                    inventory.setItem(slot, this.loseItem());
                }
                for (var slot : new int[]{8, 17, 44}) {
                    inventory.setItem(slot, this.loseItem());
                }
            }
            this.changeExit(corePlayer, inventory);
        } else if (this.animation()) {
            for (var slot : new int[]{0, 8, 9, 36, 45, 17, 44}) {
                inventory.setItem(slot, this.placeHolderItem());
            }
        } else {
            if (!corePlayer.equals(this.turn())) {
                for (var slot : new int[]{0, 9, 36}) {
                    inventory.setItem(slot, this.placeHolderItem());
                }
                for (var slot : new int[]{8, 17, 44}) {
                    inventory.setItem(slot, this.turnItem());
                }
            } else {
                corePlayer.bukkitPlayer().ifPresent(player ->
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1));

                for (var slot : new int[]{0, 9, 36}) {
                    inventory.setItem(slot, this.turnItem());
                }
                for (var slot : new int[]{8, 17, 44}) {
                    inventory.setItem(slot, this.placeHolderItem());
                }
            }
        }
    }

    @Override
    protected void changeTime() {
        for (var inventory : this.players().values()) {
            inventory.setItem(45, ItemBuilder.of(Material.CLOCK)
                    .amount(this.time())
                    .name(Component.translatable("lobby.minigame.item.time.name"))
                    .lore(Component.translatable("lobby.minigame.item.time.lore"))
                    .build());
        }
    }
}
