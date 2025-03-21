package de.taktikcrew.lobbysystem.minigames.connection.tictactoe;

import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.minigames.AbstractGameManager;
import de.taktikcrew.lobbysystem.minigames.DummyCorePlayer;

import java.util.List;

public class TicTacToeManager extends AbstractGameManager<TicTacToe> {

    public TicTacToeManager(Lobby lobby) {
        super(lobby);

        new TicTacToeListener(this);
    }

    @Override
    public void createGame(ICorePlayer corePlayer, ICorePlayer opponent) {
        var ticTacToe = new TicTacToe(this, List.of(corePlayer, opponent), false);
        this.games.put(corePlayer, ticTacToe);
        this.games.put(opponent, ticTacToe);
    }

    @Override
    public void createBotGame(ICorePlayer corePlayer) {
        var botPlayer = new DummyCorePlayer();
        var ticTacToe = new TicTacToe(this, List.of(corePlayer, botPlayer), true);
        this.games.put(corePlayer, ticTacToe);
        this.games.put(botPlayer, ticTacToe);
    }
}
