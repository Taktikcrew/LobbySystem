package de.taktikcrew.lobbysystem.jumpandrun.listener;

import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRunManager;
import de.taktikcrew.lobbysystem.jumpandrun.events.JumpAndRunFinishEvent;
import de.taktikcrew.lobbysystem.utils.Stringify;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JumpAndRunFinishListener implements Listener {

    private final JumpAndRunManager jumpAndRunManager;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Component prefix;

    public JumpAndRunFinishListener(JumpAndRunManager jumpAndRunManager) {
        this.jumpAndRunManager = jumpAndRunManager;

        this.prefix = this.jumpAndRunManager.prefix();

        this.jumpAndRunManager.lobby().getServer().getPluginManager().registerEvents(this, this.jumpAndRunManager.lobby());
    }

    @EventHandler
    public void onFinish(JumpAndRunFinishEvent event) {
        var corePlayer = event.corePlayer();
        var jumpAndRunData = event.jumpAndRunData();
        var jumpAndRun = jumpAndRunData.jumpAndRun();

        var titleKey = event.aborted() ? "jar.title.aborted" : "jar.title.finished";

        corePlayer.sendTitle(Component.text(jumpAndRun.name(), jumpAndRun.difficulty().color()),
                Component.translatable(titleKey), 2, 5, 2);

        var messageKey = event.aborted() ? "jar.message.aborted" : "jar.message.finished";

        corePlayer.message(this.prefix.append(Component.translatable(messageKey)
                .arguments(Component.text(jumpAndRun.name()), Component.text(jumpAndRun.builder()))));

        var playTime = System.currentTimeMillis() - jumpAndRunData.startTime();

        corePlayer.message(this.miniMessage.deserialize("<gray>Difficulty<dark_gray>: ")
                .append(Component.text(jumpAndRun.difficulty().name(), jumpAndRun.difficulty().color()))
                .append(this.miniMessage.deserialize(" <dark_gray>║ <gray>Playtime<dark_gray>: "))
                .append(Component.text(Stringify.time(playTime), NamedTextColor.DARK_GREEN))
                .append(this.miniMessage.deserialize(" <dark_gray>║ <gray>Fails<dark_gray>: "))
                .append(Component.text(jumpAndRunData.fails(), NamedTextColor.DARK_GREEN)));

        corePlayer.inventory().clear();
        // todo: set lobby inventory

        if (event.aborted()) {
            return;
        }
        if (jumpAndRun.recordTime() <= playTime) {
            return;
        }
        jumpAndRun.recordTime(playTime);
        this.jumpAndRunManager.jumpAndRunDAO().update(jumpAndRun);
    }
}
