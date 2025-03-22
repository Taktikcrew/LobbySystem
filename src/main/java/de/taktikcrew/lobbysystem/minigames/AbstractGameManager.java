package de.taktikcrew.lobbysystem.minigames;

import com.google.common.collect.Maps;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.minigames.connection.ConnectionGame;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public abstract class AbstractGameManager<T extends ConnectionGame> {

    private final Lobby lobby;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Component prefix;

    protected final Map<ICorePlayer, T> games;

    protected AbstractGameManager(Lobby lobby) {
        this.lobby = lobby;

        this.prefix = this.miniMessage.deserialize("<dark_gray>[<aqua>MiniGame<dark_gray>] ");

        this.games = Maps.newHashMap();
    }

    public abstract void createGame(ICorePlayer corePlayer, ICorePlayer opponent);

    public abstract void createBotGame(ICorePlayer corePlayer);

    public T game(ICorePlayer corePlayer) {
        return this.games.get(corePlayer);
    }

    public void removeGame(ICorePlayer... corePlayers) {
        Arrays.asList(corePlayers).forEach(this.games.keySet()::remove);
    }

}
