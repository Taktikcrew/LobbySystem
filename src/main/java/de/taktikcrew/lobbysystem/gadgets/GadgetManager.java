package de.taktikcrew.lobbysystem.gadgets;

import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.gadgets.listener.BootsListener;
import de.taktikcrew.lobbysystem.gadgets.meta.GadgetType;
import de.taktikcrew.lobbysystem.lobbyplayer.LobbyPlayer;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
public record GadgetManager(Lobby lobby) {

    public GadgetManager(Lobby lobby) {
        this.lobby = lobby;

        new BootsListener(this);
    }

    public void activateGadget(LobbyPlayer lobbyPlayer, @NotNull AbstractGadget gadget) {
        this.deactivateGadget(lobbyPlayer, gadget.type());

        gadget.activate(lobbyPlayer);
        gadget.active(true);

        if (lobbyPlayer.gadgets().contains(gadget)) {
            return;
        }
        lobbyPlayer.gadgets().add(gadget);
    }

    public void deactivateGadget(LobbyPlayer lobbyPlayer, GadgetType gadgetType) {
        lobbyPlayer.activeGadget(gadgetType).ifPresent(gadget -> {
            gadget.deactivate(lobbyPlayer);
            gadget.active(false);
        });
    }

}
