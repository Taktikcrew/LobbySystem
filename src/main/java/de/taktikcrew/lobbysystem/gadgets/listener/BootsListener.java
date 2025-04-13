package de.taktikcrew.lobbysystem.gadgets.listener;

import de.taktikcrew.lobbysystem.gadgets.GadgetManager;
import de.taktikcrew.lobbysystem.gadgets.boots.BootsGadget;
import de.taktikcrew.lobbysystem.gadgets.meta.GadgetType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class BootsListener implements Listener {

    private final GadgetManager gadgetManager;

    public BootsListener(GadgetManager gadgetManager) {
        this.gadgetManager = gadgetManager;

        this.gadgetManager.lobby().getServer().getPluginManager().registerEvents(this, this.gadgetManager.lobby());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!hasMoved(event)) {
            return;
        }

        for (var lobbyPlayer : this.gadgetManager.lobby().databaseProvider().lobbyPlayerDAO().cache().values()) {
            var optionalGadget = lobbyPlayer.activeGadget(GadgetType.BOOTS);
            optionalGadget.ifPresent(gadget -> {
                if (!gadget.active()) {
                    return;
                }
                ((BootsGadget) gadget).spawnEffect(lobbyPlayer);
            });
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) {
            return;
        }
        for (var lobbyPlayer : this.gadgetManager.lobby().databaseProvider().lobbyPlayerDAO().cache().values()) {
            var optionalGadget = lobbyPlayer.activeGadget(GadgetType.BOOTS);
            optionalGadget.ifPresent(gadget -> {
                if (!gadget.active()) {
                    return;
                }
                ((BootsGadget) gadget).handleSneak(lobbyPlayer);
            });
        }
    }

    private boolean hasMoved(PlayerMoveEvent event) {
        return event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                event.getFrom().y() != event.getTo().y() ||
                event.getFrom().getBlockZ() != event.getTo().getBlockZ();
    }

}
