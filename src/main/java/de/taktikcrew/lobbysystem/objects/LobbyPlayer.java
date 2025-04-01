package de.taktikcrew.lobbysystem.objects;

import de.chojo.sadu.mapper.annotation.MappingProvider;
import de.chojo.sadu.mapper.reader.StandardReader;
import de.chojo.sadu.mapper.wrapper.Row;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
public class LobbyPlayer {

    private final UUID uuid;
    private boolean dsgvoAccepted;

    public LobbyPlayer(UUID uuid) {
        this.uuid = uuid;
        this.dsgvoAccepted = false;
    }

    @MappingProvider({"uuid"})
    public LobbyPlayer(Row row) throws SQLException {
        this.uuid = row.get("uuid", StandardReader.UUID_FROM_STRING);
        this.dsgvoAccepted = row.getBoolean("dsgvoAccepted");
    }
}
