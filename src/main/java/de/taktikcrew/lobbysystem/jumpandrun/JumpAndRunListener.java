package de.taktikcrew.lobbysystem.jumpandrun;

import de.smoofy.core.api.Core;
import de.smoofy.core.api.utils.Pair;
import de.taktikcrew.lobbysystem.inventories.InventoryItemKeys;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class JumpAndRunListener implements Listener {

    private final JumpAndRunManager jumpAndRunManager;

    private final Component prefix;

    public JumpAndRunListener(JumpAndRunManager jumpAndRunManager) {
        this.jumpAndRunManager = jumpAndRunManager;

        this.prefix = this.jumpAndRunManager.prefix();

        this.jumpAndRunManager.lobby().getServer().getPluginManager().registerEvents(this, this.jumpAndRunManager.lobby());
    }

    @EventHandler
    public void onMove(@NotNull PlayerMoveEvent event) {
        var player = event.getPlayer();
        var corePlayer = Core.instance().corePlayerProvider().corePlayer(player);
        if (this.jumpAndRunManager.isInJumpAndRun(corePlayer)) {
            var jumpAndRunData = this.jumpAndRunManager.jumpAndRunData().get(corePlayer);
            var jumpAndRun = jumpAndRunData.jumpAndRun();
            if (player.getLocation().getBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
                if (player.getLocation().getBlockX() == this.jumpAndRunManager.deserialize(jumpAndRun.endLocation()).getBlockX() &&
                        player.getLocation().getBlockY() == this.jumpAndRunManager.deserialize(jumpAndRun.endLocation()).getBlockY() &&
                        player.getLocation().getBlockZ() == this.jumpAndRunManager.deserialize(jumpAndRun.endLocation()).getBlockZ()) {

                    this.jumpAndRunManager.finishJumpAndRun(corePlayer);
                }
            } else if (player.getLocation().getBlock().getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
                if (player.getLocation().getBlockX() == jumpAndRunData.checkpoint().getBlockX() &&
                        player.getLocation().getBlockY() == jumpAndRunData.checkpoint().getBlockY() &&
                        player.getLocation().getBlockZ() == jumpAndRunData.checkpoint().getBlockZ()) {
                    return;
                }
                for (var checkPoint : jumpAndRun.checkpoints()) {
                    if (player.getLocation().getBlockX() == this.jumpAndRunManager.deserialize(checkPoint).getBlockX() &&
                            player.getLocation().getBlockY() == this.jumpAndRunManager.deserialize(checkPoint).getBlockY() &&
                            player.getLocation().getBlockZ() == this.jumpAndRunManager.deserialize(checkPoint).getBlockZ()) {

                        jumpAndRunData.checkpoint(this.jumpAndRunManager.deserialize(checkPoint).clone());
                    }
                }
            }
            return;
        }
        if (!player.getLocation().getBlock().getType().equals(Material.OAK_PRESSURE_PLATE)) {
            return;
        }
        this.jumpAndRunManager.jumpAndRun(player.getLocation()).ifPresent(jumpAndRun ->
                this.jumpAndRunManager.enterJumpAndRun(corePlayer, jumpAndRun));
    }

    @EventHandler
    public void onChat(@NotNull AsyncChatEvent event) {
        var corePlayer = Core.instance().corePlayerProvider().corePlayer(event.getPlayer());
        if (this.jumpAndRunManager.notInSetup(corePlayer)) {
            return;
        }
        event.setCancelled(true);
        var message = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());
        if (message.toLowerCase().contains("cancel")) {
            this.jumpAndRunManager.setupSteps(new Pair<>(null, null));
            corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.cancelled")));
            return;
        }
        switch (this.jumpAndRunManager.setupSteps().value()) {
            case NAME -> {
                if (this.jumpAndRunManager.jumpAndRuns().containsKey(message)) {
                    corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.already_exist")));
                    return;
                }
                this.jumpAndRunManager.name(message);
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.name_set")));
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.builder")));
                this.jumpAndRunManager.setupSteps(new Pair<>(corePlayer, JumpAndRun.SetupStep.BUILDER));
            }
            case BUILDER -> {
                this.jumpAndRunManager.builder(message);
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.builder_set")));
                this.jumpAndRunManager.sendDifficulties(corePlayer);
                this.jumpAndRunManager.setupSteps(new Pair<>(corePlayer, JumpAndRun.SetupStep.DIFFICULTY));
            }
            case DIFFICULTY -> {
                JumpAndRun.Difficulty.byId(1).ifPresentOrElse(this.jumpAndRunManager::difficulty, () ->
                        this.jumpAndRunManager.sendDifficulties(corePlayer));
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.difficulty_set")));
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.start_location")));
                this.jumpAndRunManager.setupSteps(new Pair<>(corePlayer, JumpAndRun.SetupStep.START_LOCATION));
            }
            case START_LOCATION -> {
                if (!corePlayer.location().getBlock().getType().equals(Material.OAK_PRESSURE_PLATE)) {
                    corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.no_oak_pressure_plate")));
                    return;
                }
                this.jumpAndRunManager.startLocation(corePlayer.location());
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.start_location_set")));
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.end_location")));
                this.jumpAndRunManager.setupSteps(new Pair<>(corePlayer, JumpAndRun.SetupStep.END_LOCATION));
            }
            case END_LOCATION -> {
                if (!corePlayer.location().getBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
                    corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.no_light_pressure_plate")));
                    return;
                }
                this.jumpAndRunManager.endLocation(corePlayer.location());
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.end_location_set")));
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.checkpoint")));
                this.jumpAndRunManager.setupSteps(new Pair<>(corePlayer, JumpAndRun.SetupStep.CHECKPOINTS));
            }
            case CHECKPOINTS -> {
                if (!corePlayer.location().getBlock().getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
                    corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.no_heavy_pressure_plate")));
                    return;
                }
                this.jumpAndRunManager.checkpoints().add(corePlayer.location());
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.checkpoint_set")));
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.checkpoint")));
                corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.create")));
            }
        }
    }

    @EventHandler
    public void onAct(@NotNull PlayerInteractEvent event) {
        var corePlayer = Core.instance().corePlayerProvider().corePlayer(event.getPlayer());
        if (!this.jumpAndRunManager.isInJumpAndRun(corePlayer)) {
            return;
        }

        var item = event.getItem();
        if (item == null) {
            return;
        }
        if (item.getItemMeta() == null) {
            return;
        }

        var persistentDataContainer = item.getItemMeta().getPersistentDataContainer();
        if (persistentDataContainer.has(InventoryItemKeys.JAR_CHECKPOINT.key())) {
            var jumpAndRunData = this.jumpAndRunManager.jumpAndRunData().get(corePlayer);
            jumpAndRunData.addFail();
            corePlayer.bukkitPlayer().ifPresent(player -> player.teleport(jumpAndRunData.checkpoint()));
        } else if (persistentDataContainer.has(InventoryItemKeys.JAR_ABORT.key())) {
            jumpAndRunManager.abortJumpAndRun(corePlayer);
        }
    }
}