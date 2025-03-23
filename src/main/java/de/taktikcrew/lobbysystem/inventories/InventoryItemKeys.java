package de.taktikcrew.lobbysystem.inventories;

import de.taktikcrew.lobbysystem.Lobby;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

@Getter
@Accessors(fluent = true)
public enum InventoryItemKeys {

    LOBBY_NAVIGATOR("navigator"),
    LOBBY_PLAYER_HIDER("player_hider"),
    LOBBY_GADGETS("gadgets"),
    LOBBY_PROFILE("profile"),
    LOBBY_NICK("nick"),
    LOBBY_SILENT_HUB("silent_hub"),
    JAR_CHECKPOINT("jar:back_to_checkpoint"),
    JAR_ABORT("jar:abort");

    private final NamespacedKey key;
    private final Plugin plugin = Lobby.getPlugin(Lobby.class);

    InventoryItemKeys(String key) {
        this.key = NamespacedKey.fromString(key, plugin);
    }
}
