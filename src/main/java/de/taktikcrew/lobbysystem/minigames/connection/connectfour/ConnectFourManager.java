package de.taktikcrew.lobbysystem.minigames.connection.connectfour;

import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.minigames.AbstractGameManager;
import de.taktikcrew.lobbysystem.minigames.DummyCorePlayer;

import java.util.List;

public class ConnectFourManager extends AbstractGameManager<ConnectFour> {

    public ConnectFourManager(Lobby lobby) {
        super(lobby);

        new ConnectFourListener(this);
    }

    @Override
    public void createGame(ICorePlayer corePlayer, ICorePlayer opponent) {
        var connectFour = new ConnectFour(this, List.of(corePlayer, opponent), false);
        this.games.put(corePlayer, connectFour);
        this.games.put(opponent, connectFour);
    }

    @Override
    public void createBotGame(ICorePlayer corePlayer) {
        var botPlayer = new DummyCorePlayer();
        var connectFour = new ConnectFour(this, List.of(corePlayer, botPlayer), true);
        this.games.put(corePlayer, connectFour);
        this.games.put(botPlayer, connectFour);
    }
}
