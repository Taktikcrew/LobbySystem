package de.taktikcrew.lobbysystem.jumpandrun;

import com.google.common.collect.Lists;
import de.smoofy.core.api.Core;
import de.smoofy.core.api.player.ICorePlayer;
import de.smoofy.core.api.utils.Pair;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JumpAndRunCommand implements BasicCommand {

    private final JumpAndRunManager jumpAndRunManager;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public JumpAndRunCommand(JumpAndRunManager jumpAndRunManager) {
        this.jumpAndRunManager = jumpAndRunManager;

        this.jumpAndRunManager.lobby().getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("jumpandrun", this);
        });
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (!(commandSourceStack.getSender() instanceof Player player)) {
            commandSourceStack.getSender().sendMessage("§cDu musst ein Spieler sein");
            return;
        }
        var corePlayer = Core.instance().corePlayerProvider().corePlayer(player);
        if (!player.hasPermission("lobby.jar.setup")) {
            corePlayer.noPerms();
            return;
        }
        if (args.length != 1 && args.length != 2) {
            this.sendUsage(corePlayer);
            return;
        }
        var prefix = this.jumpAndRunManager.prefix();
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("setup")) {
                if (this.jumpAndRunManager.notInSetup(corePlayer)) {
                    if (this.jumpAndRunManager.setupSteps().key() != null) {
                        corePlayer.message(prefix.append(Component.translatable("jar.command.setup.other")
                                .arguments(this.jumpAndRunManager.setupSteps().key().displayName())));
                        return;
                    }
                    this.jumpAndRunManager.setupSteps(new Pair<>(corePlayer, JumpAndRun.SetupStep.NAME));
                    corePlayer.message(prefix.append(Component.translatable("jar.command.setup.name")));
                    corePlayer.message(prefix.append(Component.translatable("jar.command.setup.cancel")));
                    return;
                }
                corePlayer.message(prefix.append(Component.translatable("jar.command.setup.already")));
                switch (this.jumpAndRunManager.setupSteps().value()) {
                    case NAME -> corePlayer.message(prefix.append(Component.translatable("jar.command.setup.name")));
                    case BUILDER ->
                            corePlayer.message(prefix.append(Component.translatable("jar.command.setup.builder")));
                    case DIFFICULTY -> this.jumpAndRunManager.sendDifficulties(corePlayer);
                    case START_LOCATION ->
                            corePlayer.message(prefix.append(Component.translatable("jar.command.setup.start_location")));
                    case END_LOCATION ->
                            corePlayer.message(prefix.append(Component.translatable("jar.command.setup.end_location")));
                    case CHECKPOINTS -> {
                        corePlayer.message(prefix.append(Component.translatable("jar.command.setup.checkpoint")));
                        corePlayer.message(prefix.append(Component.translatable("jar.command.setup.create")));
                    }
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                corePlayer.message(prefix.append(this.miniMessage.deserialize("<gray>JumpAndRun's<dark_gray>:")));
                for (var jumpAndRun : this.jumpAndRunManager.jumpAndRuns().values()) {
                    corePlayer.message(this.miniMessage.deserialize(" <dark_gray>» <dark_green><b>" + jumpAndRun.name() +
                                    "</b> <gray>by <dark_green>" + jumpAndRun.builder() + " <dark_gray>║ ")
                            .append(Component.text(jumpAndRun.difficulty().name(), jumpAndRun.difficulty().color())));
                }
            } else if (args[0].equalsIgnoreCase("create")) {
                if (this.jumpAndRunManager.name() == null || this.jumpAndRunManager.startLocation() == null || this.jumpAndRunManager.endLocation() == null) {
                    corePlayer.message(prefix.append(Component.translatable("jar.command.setup.not_finished")));
                    return;
                }
                var jumpAndRun = new JumpAndRun(this.jumpAndRunManager.name(),
                        this.jumpAndRunManager.builder(),
                        this.jumpAndRunManager.difficulty(),
                        this.jumpAndRunManager.startLocation(),
                        this.jumpAndRunManager.endLocation(),
                        this.jumpAndRunManager.checkpoints());
                this.jumpAndRunManager.jumpAndRunDAO().create(jumpAndRun);

                corePlayer.message(Component.translatable("jar.command.setup.created"));
                this.jumpAndRunManager.setupSteps(new Pair<>(null, null));
                this.jumpAndRunManager.name(null);
                this.jumpAndRunManager.builder(null);
                this.jumpAndRunManager.difficulty(null);
                this.jumpAndRunManager.startLocation(null);
                this.jumpAndRunManager.endLocation(null);
                this.jumpAndRunManager.checkpoints(Lists.newArrayList());
            } else {
                this.sendUsage(corePlayer);
            }
        } else {
            if (args[0].equalsIgnoreCase("delete")) {
                this.jumpAndRunManager.jumpAndRun(args[1]).ifPresentOrElse(_ -> {
                    this.jumpAndRunManager.jumpAndRunDAO().delete(args[1]);
                    corePlayer.message(Component.translatable("jar.command.setup.deleted"));
                }, () -> corePlayer.message(Component.translatable("jar.command.setup.not_exist")));
            } else if (args[0].equalsIgnoreCase("toggle")) {
                this.jumpAndRunManager.jumpAndRun(args[1]).ifPresent(jumpAndRun -> {
                    jumpAndRun.playable(!jumpAndRun.playable());
                    this.jumpAndRunManager.jumpAndRunDAO().update(jumpAndRun);
                    corePlayer.message(jumpAndRun.playable() ?
                            Component.translatable("jar.command.setup.toggled_on") :
                            Component.translatable("jar.command.setup.toggled_off"));
                });
            } else {
                this.sendUsage(corePlayer);
            }
        }
    }

    private void sendUsage(ICorePlayer corePlayer) {
        corePlayer.usage(Component.text("JAR", NamedTextColor.DARK_GREEN), "/jar <setup/list/create/delete/toggle> [name]");
    }
}