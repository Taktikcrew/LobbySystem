package de.taktikcrew.lobbysystem.lobbyplayer;

import com.google.common.collect.Lists;
import de.chojo.sadu.mapper.annotation.MappingProvider;
import de.chojo.sadu.mapper.reader.StandardReader;
import de.chojo.sadu.mapper.wrapper.Row;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.query.Query;
import de.chojo.sadu.queries.call.adapter.UUIDAdapter;
import de.taktikcrew.lobbysystem.settings.AbstractSetting;
import de.taktikcrew.lobbysystem.settings.meta.SettingFactory;
import de.taktikcrew.lobbysystem.settings.meta.SettingKey;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
public class LobbyPlayer {

    private final UUID uuid;
    private boolean dsgvoAccepted;
    private List<AbstractSetting<?>> settings = Lists.newArrayList();

    public LobbyPlayer(UUID uuid) {
        this.uuid = uuid;
        this.dsgvoAccepted = false;
    }

    @MappingProvider({"uuid"})
    public LobbyPlayer(Row row) throws SQLException {
        this.uuid = row.get("uuid", StandardReader.UUID_FROM_STRING);
        this.dsgvoAccepted = row.getBoolean("dsgvoAccepted");
        this.settings = this.loadSettings();
    }

    public Optional<AbstractSetting<?>> settingByKey(SettingKey settingKey) {
        return this.settings.stream().filter(setting -> setting.settingKey().equals(settingKey)).findFirst();
    }

    private List<AbstractSetting<?>> loadSettings() {
        return new ArrayList<>(Query.query("SELECT * FROM LobbyPlayer_settings WHERE uuid = ?")
                .single(Call.of().bind(this.uuid, UUIDAdapter.AS_STRING))
                .map(row -> SettingFactory.settingOfKey(this.uuid, row.getString("setting"), row.getString("state")))
                .all());
    }
}
