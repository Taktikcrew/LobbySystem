package de.taktikcrew.lobbysystem.minigames.connection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.smoofy.core.api.Core;
import de.smoofy.core.api.builder.InventoryBuilder;
import de.smoofy.core.api.builder.ItemBuilder;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.minigames.AbstractGameManager;
import de.taktikcrew.lobbysystem.minigames.DummyCorePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

@Getter
@Accessors(fluent = true)
public abstract class ConnectionGame {

    private final AbstractGameManager<?> abstractGameManager;
    private final Type type;

    private final Component prefix;

    private final Map<ICorePlayer, Inventory> players;
    private final Map<ICorePlayer, Material> playerSkin;

    private ICorePlayer turn;
    private final boolean botMatch;
    private boolean started;
    @Setter
    private boolean finished;

    @Setter
    private boolean animation;

    private BukkitTask bukkitTask;
    private int time;

    @Setter
    private ICorePlayer rematch;

    private final ItemStack placeHolderItem = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).noName().build();
    private final ItemStack turnItem = ItemBuilder.of(Material.YELLOW_STAINED_GLASS_PANE).name(Component.translatable("lobby.minigame.item.turn.name")).build();
    private final ItemStack loseItem = ItemBuilder.of(Material.RED_STAINED_GLASS_PANE).name(Component.translatable("lobby.minigame.item.lose.name")).build();
    private final ItemStack winItem = ItemBuilder.of(Material.LIME_STAINED_GLASS_PANE).name(Component.translatable("lobby.minigame.item.win.name")).build();

    public ConnectionGame(AbstractGameManager<?> abstractGameManager, Type type, List<ICorePlayer> players, boolean botMatch) {
        this.abstractGameManager = abstractGameManager;
        this.type = type;

        this.prefix = this.abstractGameManager.prefix();

        this.players = Maps.newHashMap();
        this.playerSkin = Maps.newHashMap();

        this.turn = players.get(new Random().nextInt(2));
        this.botMatch = botMatch;
        this.started = false;
        this.finished = false;

        this.rematch = null;

        this.createInventories(players);
    }

    protected abstract void place(ICorePlayer corePlayer, int slot);

    protected abstract boolean isPlaceable(int slot);

    protected abstract int botMove();

    protected abstract List<Integer> checkHorizontal(Inventory inventory, Material skin, int slot);

    protected abstract List<Integer> checkVertical(Inventory inventory, Material skin, int slot);

    protected abstract List<Integer> checkDiagonalLeftToRight(Inventory inventory, Material skin, int slot);

    protected abstract List<Integer> checkDiagonalRightToLeft(Inventory inventory, Material skin, int slot);

    protected abstract void changeExit(ICorePlayer corePlayer, Inventory inventory);

    protected abstract void changeGlass(ICorePlayer corePlayer, Inventory inventory);

    protected abstract void changeTime();

    protected void changeTurn() {
        this.turn = this.otherPlayer(this.turn);
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
            this.place(this.turn, this.botMove());
        }
        this.schedule();
    }

    protected boolean noWinnerExists(Inventory inventory, Material skin, int slot) {
        return Stream.of(
                this.checkVertical(inventory, skin, slot),
                this.checkHorizontal(inventory, skin, slot),
                this.checkDiagonalLeftToRight(inventory, skin, slot),
                this.checkDiagonalRightToLeft(inventory, skin, slot)
        ).filter(slots -> slots.size() >= this.type.itemsToWin).findFirst().map(slots -> {
            this.finish(slots);
            return true;
        }).orElse(false);
    }

    private void finish(List<Integer> slots) {
        this.finished = true;
        for (var corePlayer : this.players.keySet()) {
            var otherPlayer = this.otherPlayer(corePlayer);

            if (turn.equals(corePlayer)) {
                corePlayer.bukkitPlayer().ifPresent(player -> player.playSound(player.getLocation(),
                        Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1));
                corePlayer.message(this.prefix.append(Component.translatable("minigame.message.win")
                        .arguments(otherPlayer.displayName())));
            } else {
                corePlayer.bukkitPlayer().ifPresent(player -> player.playSound(player.getLocation(),
                        Sound.BLOCK_BEACON_DEACTIVATE, 1, 1));
                corePlayer.message(this.prefix.append(Component.translatable("minigame.message.lose")
                        .arguments(otherPlayer.displayName())));
            }
            var playerInventory = this.players.get(corePlayer);
            this.changeGlass(corePlayer, playerInventory);
            for (int i : slots) {
                this.changeWinnerItem(playerInventory, i);
            }
        }
    }

    private void draw() {
        this.finished = true;
        for (var corePlayer : this.players.keySet()) {
            var otherPlayer = this.otherPlayer(corePlayer);
            corePlayer.message(this.prefix.append(Component.translatable("minigame.message.draw")
                    .arguments(otherPlayer.displayName())));

            var playerInventory = this.players.get(corePlayer);
            this.changeExit(corePlayer, playerInventory);
        }
    }

    protected @NotNull ICorePlayer otherPlayer(ICorePlayer corePlayer) {
        for (var otherPlayer : this.players.keySet()) {
            if (!otherPlayer.equals(corePlayer)) {
                return otherPlayer;
            }
        }
        return new DummyCorePlayer();
    }

    private void changeWinnerItem(@NotNull Inventory inventory, int slot) {
        var item = inventory.getItem(slot);
        if (item == null) {
            return;
        }
        inventory.setItem(slot, ItemBuilder.of(item)
                .name(item.displayName().color(NamedTextColor.GOLD))
                .enchantUnsafe(1, Enchantment.INFINITY)
                .itemFlags(ItemFlag.HIDE_ENCHANTS)
                .build());
    }

    protected void schedule() {
        if (this.bukkitTask != null) {
            this.bukkitTask.cancel();
        }

        this.time = 15;
        this.changeTime();
        this.bukkitTask = Core.instance().coreTask().repeat(() -> {
            this.time--;
            this.changeTime();

            if (this.time == 0) {
                if (this.finished()) {
                    for (var corePlayer : this.players.keySet()) {
                        this.abstractGameManager.removeGame(corePlayer);
                        corePlayer.bukkitPlayer().ifPresent(player -> {
                            player.closeInventory();
                            if (!corePlayer.equals(this.turn)) {
                                corePlayer.message(this.prefix.append(Component.translatable("minigame.message.time_over")));
                            }
                        });
                    }
                } else {
                    this.turn = this.otherPlayer(this.turn);
                    this.finish(Lists.newArrayList());
                    this.schedule();
                }
                this.bukkitTask.cancel();
            }
        }, 20, 20);
    }

    protected void selectSkin(ICorePlayer corePlayer, int slot, Material material) {
        this.playerSkin.put(corePlayer, material);
        for (var inventory : this.players.values()) {
            inventory.setItem(slot, ItemBuilder.of(Material.BARRIER)
                    .name(Component.translatable("lobby.minigame.item.chosen.name"))
                    .build());
        }
        if (this.playerSkin.containsKey(this.otherPlayer(corePlayer))) {
            this.started = true;
            this.createInventories(Arrays.asList(this.players.keySet().toArray(new ICorePlayer[]{})));
            this.changeTurn();
        }
    }

    private void createInventories(List<ICorePlayer> players) {
        if (this.started) {
            this.players.put(players.getFirst(), this.createInventory(players.getFirst(), players.get(1)));
            this.players.put(players.get(1), this.createInventory(players.get(1), players.getFirst()));
            for (var corePlayer : this.players.keySet()) {
                corePlayer.bukkitPlayer().ifPresent(player -> player.openInventory(this.players.get(corePlayer)));
            }
        } else {
            this.players.put(players.getFirst(), this.createSkinSelector(players.getFirst(), players.get(1)));
            this.players.put(players.get(1), this.createSkinSelector(players.get(1), players.getFirst()));
            for (var corePlayer : this.players.keySet()) {
                corePlayer.bukkitPlayer().ifPresent(player -> player.openInventory(this.players.get(corePlayer)));
                if (this.botMatch && corePlayer.name().equals("Bot")) {
                    this.selectSkin(corePlayer, 29, Material.SKELETON_SKULL);
                }
            }
        }
    }

    protected Inventory createInventory(@NotNull ICorePlayer corePlayer, @NotNull ICorePlayer opponent) {
        return InventoryBuilder.of(Component.translatable("minigame.menu.game.title")
                        .arguments(this.type.gameName, opponent.displayName()), 6)

                .set(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).noName(), 0, 8, 9, 17, 36, 44, 53)
                .set(ItemBuilder.of(Material.PLAYER_HEAD).skullOwner(corePlayer).name(corePlayer.displayName()), 18)
                .set(ItemBuilder.of(this.skin(corePlayer)).name(Component.translatable("lobby.minigame.item.skin.name")), 27)
                .set(ItemBuilder.of(Material.PLAYER_HEAD).skullOwner(opponent).name(opponent.displayName()), 26)
                .set(ItemBuilder.of(this.skin(opponent)).name(Component.translatable("lobby.minigame.item.skin.name")), 35)
                .set(ItemBuilder.of(Material.SPRUCE_DOOR).name(Component.translatable("lobby.minigame.item.quit.name")).lore(), 53)
                .build();
    }

    protected void createRematchInventories() {
        for (var corePlayer : this.players.keySet()) {
            if (!this.rematch.equals(corePlayer)) {
                this.players.put(corePlayer, this.createRematchInventory(this.rematch, false));
            } else {
                this.players.put(this.rematch, this.createRematchInventory(this.rematch, true));
            }
            corePlayer.bukkitPlayer().ifPresent(player -> player.openInventory(this.players.get(corePlayer)));
        }
    }

    private Inventory createRematchInventory(ICorePlayer corePlayer, boolean requester) {
        var otherPlayer = this.otherPlayer(corePlayer);
        if (requester) {
            return InventoryBuilder.of(Component.translatable("minigame.menu.game.title")
                            .arguments(this.type.gameName, otherPlayer.displayName()), 6)

                    .fill(ItemBuilder.of(Material.CLOCK)
                            .name(Component.translatable("lobby.minigame.item.rematch_wait.name")
                                    .arguments(otherPlayer.displayName()))
                            .lore(Component.translatable("lobby.minigame.item.rematch_wait.lore")))
                    .build();
        }
        return InventoryBuilder.of(Component.translatable("minigame.menu.game.title")
                        .arguments(this.type.gameName, otherPlayer.displayName()), 6)

                .fill(ItemBuilder.of(Material.FIREWORK_ROCKET)
                        .name(Component.translatable("lobby.minigame.item.rematch_accept.name")
                                .arguments(corePlayer.displayName()))
                        .lore(Component.translatable("lobby.minigame.item.rematch_accept.lore")))
                .build();
    }

    private Inventory createSkinSelector(@NotNull ICorePlayer corePlayer, @NotNull ICorePlayer opponent) {
        return InventoryBuilder.of(Component.translatable("minigame.menu.game.title")
                        .arguments(this.type.gameName, opponent.displayName()), 6)

                .fill(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).noName())
                .set(ItemBuilder.of(Material.PLAYER_HEAD).skullOwner(corePlayer).name(corePlayer.displayName()), 18)
                .set(ItemBuilder.of(Material.PLAYER_HEAD).skullOwner(opponent).name(opponent.displayName()), 26)

                .set(ItemBuilder.of(Material.DARK_OAK_SIGN)
                        .name(Component.translatable("lobby.minigame.item.information.name"))
                        .lore(Component.translatable("lobby.minigame.item.information.lore")), 0)

                .set(ItemBuilder.of(Material.BLUE_TERRACOTTA).lore(Component.translatable("lobby.minigame.item.choose.lore")), 11)
                .set(ItemBuilder.of(Material.RED_TERRACOTTA).lore(Component.translatable("lobby.minigame.item.choose.lore")), 12)
                .set(ItemBuilder.of(Material.END_PORTAL_FRAME).lore(Component.translatable("lobby.minigame.item.choose.lore")), 13)
                .set(ItemBuilder.of(Material.HAY_BLOCK).lore(Component.translatable("lobby.minigame.item.choose.lore")), 14)
                .set(ItemBuilder.of(Material.OBSIDIAN).lore(Component.translatable("lobby.minigame.item.choose.lore")), 15)
                .set(ItemBuilder.of(Material.IRON_BLOCK).lore(Component.translatable("lobby.minigame.item.choose.lore")), 20)
                .set(ItemBuilder.of(Material.GOLD_BLOCK).lore(Component.translatable("lobby.minigame.item.choose.lore")), 21)
                .set(ItemBuilder.of(Material.DIAMOND_BLOCK).lore(Component.translatable("lobby.minigame.item.choose.lore")), 22)
                .set(ItemBuilder.of(Material.EMERALD_BLOCK).lore(Component.translatable("lobby.minigame.item.choose.lore")), 23)
                .set(ItemBuilder.of(Material.TNT).lore(Component.translatable("lobby.minigame.item.choose.lore")), 24)
                .set(ItemBuilder.of(Material.SKELETON_SKULL).lore(Component.translatable("lobby.minigame.item.choose.lore")), 29)
                .set(ItemBuilder.of(Material.ZOMBIE_HEAD).lore(Component.translatable("lobby.minigame.item.choose.lore")), 30)
                .set(ItemBuilder.of(Material.CREEPER_HEAD).lore(Component.translatable("lobby.minigame.item.choose.lore")), 31)
                .set(ItemBuilder.of(Material.WITHER_SKELETON_SKULL).lore(Component.translatable("lobby.minigame.item.choose.lore")), 32)
                .set(ItemBuilder.of(Material.DRAGON_HEAD).lore(Component.translatable("lobby.minigame.item.choose.lore")), 33)
                .set(ItemBuilder.of(Material.NETHER_STAR).lore(Component.translatable("lobby.minigame.item.choose.lore")), 38)
                .set(ItemBuilder.of(Material.ENCHANTED_GOLDEN_APPLE).lore(Component.translatable("lobby.minigame.item.choose.lore")), 39)
                .set(ItemBuilder.of(Material.ENDER_EYE).lore(Component.translatable("lobby.minigame.item.choose.lore")), 40)
                .set(ItemBuilder.of(Material.MUSHROOM_STEW).lore(Component.translatable("lobby.minigame.item.choose.lore")), 41)
                .set(ItemBuilder.of(Material.DRAGON_EGG).lore(Component.translatable("lobby.minigame.item.choose.lore")), 42)

                .set(ItemBuilder.of(Material.SPRUCE_DOOR)
                        .name(Component.translatable("lobby.minigame.item.quit.name"))
                        .lore(Component.translatable("lobby.minigame.item.quit.lore")), 53)

                .build();
    }

    protected Material skin(ICorePlayer corePlayer) {
        return this.playerSkin.get(corePlayer);
    }

    @Getter
    @Accessors(fluent = true)
    @AllArgsConstructor
    public enum Type {

        CONNECT_FOUR(Component.text("Connect Four"), 4, true),
        TIC_TAC_TOE(Component.text("Tic Tac Toe"), 3, false);

        private final Component gameName;
        private final int itemsToWin;
        private final boolean animated;
    }
}
