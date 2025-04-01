package de.taktikcrew.lobbysystem.database;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mariadb.databases.MariaDb;
import de.chojo.sadu.mariadb.mapper.MariaDbMapper;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.updater.SqlUpdater;
import de.smoofy.core.api.Core;
import de.smoofy.core.api.logger.enumeration.LogType;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRunDAO;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@Getter
@Accessors(fluent = true)
public class DatabaseProvider {

    private HikariDataSource dataSource;

    private final LobbyPlayerDAO lobbyPlayerDAO;
    private final JumpAndRunDAO jumpAndRunDAO;

    public DatabaseProvider() {
        this.lobbyPlayerDAO = new LobbyPlayerDAO();
        this.jumpAndRunDAO = new JumpAndRunDAO();
    }

    public void create() {
        this.dataSource = DataSourceCreator.create(MariaDb.get())
                .configure(config -> config
                        .host("localhost")
                        .port(3306)
                        .user("Developer")
                        .password("DevTaktikcrewServer")
                        .database("Taktikcrew")
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
}
