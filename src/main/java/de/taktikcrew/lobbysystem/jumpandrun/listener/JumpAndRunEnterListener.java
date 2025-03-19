package de.taktikcrew.lobbysystem.jumpandrun.listener;

import de.smoofy.core.api.builder.ItemBuilder;
import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRunManager;
import de.taktikcrew.lobbysystem.jumpandrun.events.JumpAndRunEnterEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class JumpAndRunEnterListener implements Listener {

    private final JumpAndRunManager jumpAndRunManager;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Component prefix;

    public JumpAndRunEnterListener(JumpAndRunManager jumpAndRunManager) {
        this.jumpAndRunManager = jumpAndRunManager;

        this.prefix = this.jumpAndRunManager.prefix();

        this.jumpAndRunManager.lobby().getServer().getPluginManager().registerEvents(this, this.jumpAndRunManager.lobby());
    }

    @EventHandler
    public void onEnter(JumpAndRunEnterEvent event) {
        var corePlayer = event.corePlayer();
        var jumpAndRun = event.jumpAndRun();

        corePlayer.sendTitle(Component.text(jumpAndRun.name(), jumpAndRun.difficulty().color()),
                Component.translatable("jar.title.entered"), 1, 3, 1);

        corePlayer.message(this.prefix.append(Component.translatable("jar.message.entered")
                .arguments(Component.text(jumpAndRun.name()), Component.text(jumpAndRun.builder()))));

        corePlayer.message(this.prefix.append(this.miniMessage.deserialize("<gray>Difficulty<dark_gray>: ")
                .append(Component.text(jumpAndRun.difficulty().name(), jumpAndRun.difficulty().color()))
                .append(this.miniMessage.deserialize(" <dark_gray>║ <gray>Checkpoints<dark_gray>: "))
                .append(Component.text(jumpAndRun.checkpoints().size(), NamedTextColor.DARK_GREEN))));

        corePlayer.inventory().addItem(ItemBuilder.of(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .name(Component.translatable("lobby.jar.item.checkpoint.name"))
                .namespacedKey(this.jumpAndRunManager.checkpointKey(), PersistentDataType.BOOLEAN, true)
                .build());

        corePlayer.inventory().addItem(ItemBuilder.of(Material.BARRIER)
                .name(Component.translatable("lobby.jar.item.abort.name"))
                .namespacedKey(this.jumpAndRunManager.abortKey(), PersistentDataType.BOOLEAN, true)
                .build());
    }
}
