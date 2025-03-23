package de.taktikcrew.lobbysystem.inventories;

import de.taktikcrew.lobbysystem.Lobby;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class InventoryProvider {

    private final Lobby lobby;

    private final DsgvoInventory dsgvoInventory;
    private final LobbyPlayerInventory lobbyPlayerInventory;

    public InventoryProvider(Lobby lobby) {
        this.lobby = lobby;

        this.dsgvoInventory = new DsgvoInventory(this);
        this.lobbyPlayerInventory = new LobbyPlayerInventory(this);
    }
}
