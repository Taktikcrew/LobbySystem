package de.taktikcrew.lobbysystem.minigames;

import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.minigames.connection.connectfour.ConnectFourManager;
import de.taktikcrew.lobbysystem.minigames.connection.tictactoe.TicTacToeManager;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class MiniGameProvider {

    private final Lobby lobby;

    private final ConnectFourManager connectFourManager;
    private final TicTacToeManager ticTacToeManager;

    public MiniGameProvider(Lobby lobby) {
        this.lobby = lobby;

        this.connectFourManager = new ConnectFourManager(this.lobby);
        this.ticTacToeManager = new TicTacToeManager(this.lobby);
    }
}
