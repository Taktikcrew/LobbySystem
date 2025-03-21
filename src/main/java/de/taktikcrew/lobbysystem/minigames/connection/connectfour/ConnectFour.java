package de.taktikcrew.lobbysystem.minigames.connection.connectfour;

import com.google.common.collect.Lists;
import de.smoofy.core.api.builder.ItemBuilder;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.minigames.AbstractGameManager;
import de.taktikcrew.lobbysystem.minigames.connection.ConnectionGame;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

@Getter
@Accessors(fluent = true)
public class ConnectFour extends ConnectionGame {

    public ConnectFour(AbstractGameManager<?> abstractGameManager, List<ICorePlayer> players, boolean botMatch) {
        super(abstractGameManager, Type.CONNECT_FOUR, players, botMatch);
    }

    @Override
    public void place(ICorePlayer corePlayer, int slot) {
        this.animation(true);

        var item = ItemBuilder.of(this.skin(corePlayer)).name(corePlayer.displayName()).build();
        this.lastSlot(slot);
        for (var inventory : this.players().values()) {
            inventory.setItem(this.lastSlot(), item);
            this.changeGlass(null, inventory);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                var inventories = players().values().toArray(new Inventory[]{});
                var inventory = inventories[0];
                if (inventory.getSize() > lastSlot() + 9 && inventory.getItem(lastSlot() + 9) == null) {
                    if (inventory.getItem(lastSlot()) != null) {
                        for (var inventory2 : inventories) {
                            inventory2.setItem(lastSlot(), null);
                        }
                    }
                    for (var inventory2 : inventories) {
                        inventory2.setItem(lastSlot() + 9, item);
                    }
                    lastSlot(lastSlot() + 9);
                } else {
                    if (!checkWon(inventory, item.getType(), lastSlot())) {
                        animation(false);
                        changeTurn();
                    }
                    cancel();
                }
            }
        }.runTaskTimer(this.abstractGameManager().lobby(), 10, 10);
    }

    @Override
    public boolean isPlaceable(int slot) {
        return slot % 9 > 0;
    }

    @Override
    public List<Integer> checkHorizontal(Inventory inventory, Material skin, int slot) {
        var y = slot / 9;
        List<Integer> slots = Lists.newArrayList();
        slots.add(slot);

        for (int i = (slot - 1); i > (slot - 5); i--) {
            if (i / 9 != y || i < 1) {
                break;
            }
            if (i % 9 > 0 && i % 9 < 8) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            } else {
                break;
            }
        }

        for (int i = (slot + 1); i < (slot + 5); i++) {
            if (i / 9 != y || i > 53) {
                break;
            }
            if (i % 9 > 0 && i % 9 < 8) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            } else {
                break;
            }
        }

        return slots;
    }

    @Override
    public List<Integer> checkVertical(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        slots.add(slot);

        for (int i = (slot - 9); i < (slot - (5 * 9)); i -= 9) {
            if (i < 0) {
                break;
            }
            if (i % 9 > 0 && i % 9 < 8) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            } else {
                break;
            }
        }

        for (int i = (slot + 9); i < (slot + (5 * 9)); i += 9) {
            if (i > 53) {
                break;
            }
            if (i % 9 > 0 && i % 9 < 8) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            } else {
                break;
            }
        }

        return slots;
    }

    @Override
    public List<Integer> checkDiagonalLeftToRight(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        slots.add(slot);

        for (int i = (slot - 8); i > (slot - (5 * 8)); i -= 8) {
            if (i < 0) {
                break;
            }
            if (i % 9 > 0 && i % 9 < 8) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            } else {
                break;
            }
        }

        for (int i = (slot + 8); i < (slot + (5 * 8)); i += 8) {
            if (i > 53) {
                break;
            }
            if (i % 9 > 0 && i % 9 < 8) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            } else {
                break;
            }
        }

        return slots;
    }

    @Override
    public List<Integer> checkDiagonalRightToLeft(Inventory inventory, Material skin, int slot) {
        List<Integer> slots = Lists.newArrayList();
        slots.add(slot);

        for (int i = (slot - 10); i > (slot - (5 * 10)); i -= 10) {
            if (i < 0) {
                break;
            }
            if (i % 9 > 0 && i % 9 < 8) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            } else {
                break;
            }
        }

        for (int i = (slot + 10); i < (slot + (5 * 10)); i += 10) {
            if (i > 53) {
                break;
            }
            if (i % 9 > 0 && i % 9 < 8) {
                var item = inventory.getItem(i);
                if (item == null || !item.getType().equals(skin)) {
                    break;
                }
                slots.add(i);
            } else {
                break;
            }
        }

        return slots;
    }

    @Override
    public void changeExit(ICorePlayer corePlayer, Inventory inventory) {
        inventory.setItem(53, ItemBuilder.of(Material.FIREWORK_ROCKET)
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

        if (this.finished()) {
            if (!corePlayer.equals(this.turn())) {
                for (var slot : new int[]{0, 9, 36}) {
                    inventory.setItem(slot, lose);
                }
                for (var slot : new int[]{8, 17, 44}) {
                    inventory.setItem(slot, win);
                }
            } else {
                for (var slot : new int[]{0, 9, 36}) {
                    inventory.setItem(slot, win);
                }
                for (var slot : new int[]{8, 17, 44}) {
                    inventory.setItem(slot, lose);
                }
            }
            this.changeExit(corePlayer, inventory);
        } else if (this.animation()) {
            for (var slot : new int[]{0, 8, 9, 36, 45, 17, 44}) {
                inventory.setItem(slot, gray);
            }
        } else {
            if (!corePlayer.equals(this.turn())) {
                for (var slot : new int[]{0, 9, 36}) {
                    inventory.setItem(slot, gray);
                }
                for (var slot : new int[]{8, 17, 44}) {
                    inventory.setItem(slot, turn);
                }
            } else {
                corePlayer.bukkitPlayer().ifPresent(player ->
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1));

                for (var slot : new int[]{0, 9, 36}) {
                    inventory.setItem(slot, turn);
                }
                for (var slot : new int[]{8, 17, 44}) {
                    inventory.setItem(slot, gray);
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
            var random = new Random();
            var slot = random.nextInt(7) + 1;
            while (this.players().get(this.turn()).getItem(slot) != null) {
                slot = random.nextInt(7) + 1;
            }
            this.place(this.turn(), slot);
        }
        this.schedule();
    }

    @Override
    public void changeTime() {
        for (var inventory : this.players().values()) {
            inventory.setItem(45, ItemBuilder.of(Material.CLOCK)
                    .amount(this.time())
                    .name(Component.translatable("lobby.minigame.item.time.name"))
                    .lore(Component.translatable("lobby.minigame.item.time.lore"))
                    .build());
        }
    }
}
