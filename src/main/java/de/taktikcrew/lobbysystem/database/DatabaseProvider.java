package de.taktikcrew.lobbysystem.database;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mariadb.databases.MariaDb;
import de.chojo.sadu.mariadb.mapper.MariaDbMapper;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.updater.SqlUpdater;
import de.smoofy.core.api.Core;
import de.smoofy.core.api.config.IConfig;
import de.smoofy.core.api.logger.enumeration.LogType;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRunDAO;
import de.taktikcrew.lobbysystem.lobbyplayer.LobbyPlayerDAO;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@Getter
@Accessors(fluent = true)
public class DatabaseProvider {

    private HikariDataSource dataSource;

    private IConfig config;

    private final LobbyPlayerDAO lobbyPlayerDAO;
    private final JumpAndRunDAO jumpAndRunDAO;

    public DatabaseProvider() {
        this.loadConfig();

        this.lobbyPlayerDAO = new LobbyPlayerDAO();
        this.jumpAndRunDAO = new JumpAndRunDAO();
    }

    public void create() {
        this.dataSource = DataSourceCreator.create(MariaDb.get())
                .configure(config -> config
                        .host(this.config.get("host", String.class))
                        .port(this.config.get("port", Integer.class))
                        .user(this.config.get("user", String.class))
                        .password(this.config.get("password", String.class))
                        .database(this.config.get("database", String.class))
                )
                .create()
                .withMaximumPoolSize(3)
                .withMinimumIdle(1)
                .build();

        var config = QueryConfiguration.builder(dataSource)
                .setExceptionHandler(err -> log.error("An error occurred during a database query", err))
                .setThrowExceptions(true)
                .setRowMapperRegistry(new RowMapperRegistry().register(MariaDbMapper.getDefaultMapper()))
                .build();

        QueryConfiguration.setDefault(config);

        this.update();
    }

    private void update() {
        try {
            SqlUpdater.builder(this.dataSource, MariaDb.get())
                    .setVersionTable("lobby_version")
                    .execute();
        } catch (SQLException | IOException e) {
            Core.instance().logger(Lobby.getPlugin(Lobby.class)).log(LogType.ERROR, e.getMessage());
        }
    }

    public void close() {
        dataSource.close();
    }

    private void loadConfig() {
        File directory = new File(Bukkit.getPluginsFolder().getAbsolutePath() + "/LobbySystem");
        directory.mkdirs();
        this.config = Core.instance().config(directory, "mariadb.json");
        this.config.load();
        this.config.addIfNotExists("host", "127.0.0.1");
        this.config.addIfNotExists("port", 3306);
        this.config.addIfNotExists("user", "root");
        this.config.addIfNotExists("password", "root");
        this.config.addIfNotExists("database", "test");
        this.config.save();
    }
}
