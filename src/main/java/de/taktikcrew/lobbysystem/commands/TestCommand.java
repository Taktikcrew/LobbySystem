package de.taktikcrew.lobbysystem.commands;

import com.google.common.collect.Maps;
import de.smoofy.core.api.Core;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.gadgets.boots.LoveBoots;
import de.taktikcrew.lobbysystem.gadgets.meta.GadgetFactory;
import de.taktikcrew.lobbysystem.gadgets.meta.GadgetType;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Map;

public class TestCommand implements BasicCommand {

    private final Lobby lobby;

    private Map<ICorePlayer, ICorePlayer> requests = Maps.newHashMap();

    public TestCommand(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        if (!(commandSourceStack.getSender() instanceof Player player)) {
            commandSourceStack.getSender().sendMessage("§cDu musst ein Spieler sein");
            return;
        }
        if (args.length != 2) {
            return;
        }

        var corePlayer = Core.instance().corePlayerProvider().corePlayer(player);
        var connectFourManager = this.lobby.miniGameProvider().connectFourManager();
        var ticTacToeManager = this.lobby.miniGameProvider().ticTacToeManager();
        var gadgetManager = this.lobby.gadgetManager();
        if (args[0].equalsIgnoreCase("c4")) {
            if (args[1].equalsIgnoreCase("bot")) {
                connectFourManager.createBotGame(corePlayer);
            } else {
                var opponent = Core.instance().corePlayerProvider().corePlayer(args[1]);
                if (opponent == null) {
                    return;
                }
                if (this.requests.containsKey(opponent) && this.requests.get(opponent).equals(corePlayer)) {
                    connectFourManager.createGame(corePlayer, opponent);
                    this.requests.remove(opponent);
                } else {
                    this.requests.put(corePlayer, opponent);
                    corePlayer.message(Component.text("Du hast " + opponent.name() + " herausgefordert"));
                    opponent.message(Component.text("Du wurdest von " + corePlayer.name() + " herausgefordert"));
                }
            }
        } else if (args[0].equalsIgnoreCase("ttt")) {
            if (args[1].equalsIgnoreCase("bot")) {
                ticTacToeManager.createBotGame(corePlayer);
            } else {
                var opponent = Core.instance().corePlayerProvider().corePlayer(args[1]);
                if (opponent == null) {
                    return;
                }
                if (this.requests.containsKey(opponent) && this.requests.get(opponent).equals(corePlayer)) {
                    ticTacToeManager.createGame(corePlayer, opponent);
                    this.requests.remove(opponent);
                } else {
                    this.requests.put(corePlayer, opponent);
                    corePlayer.message(Component.text("Du hast " + opponent.name() + " herausgefordert"));
                    opponent.message(Component.text("Du wurdest von " + corePlayer.name() + " herausgefordert"));
                }
            }
        } else if (args[0].equalsIgnoreCase("boots")) {
            if (args[1].equalsIgnoreCase("love")) {
                var optionalLobbyPlayer = this.lobby.databaseProvider().lobbyPlayerDAO().get(corePlayer.uuid());
                if (optionalLobbyPlayer.isEmpty()) {
                    return;
                }
                var lobbyPlayer = optionalLobbyPlayer.get();
                var currentActive = lobbyPlayer.activeGadget(GadgetType.BOOTS);
                if (currentActive.isPresent()) {
                    gadgetManager.deactivateGadget(lobbyPlayer, GadgetType.BOOTS);
                } else {
                    var gadget = GadgetFactory.gadget(LoveBoots.class.getSimpleName(), true);
                    if (gadget == null) {
                        return;
                    }
                    gadgetManager.activateGadget(lobbyPlayer, gadget);
                }
            }
        }
    }
}
