package de.taktikcrew.lobbysystem.settings;

import de.taktikcrew.lobbysystem.settings.meta.SettingKey;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public abstract class AbstractSetting<T> {

    private final UUID uuid;
    private final SettingKey settingKey;
    @Setter
    private T state;

    public AbstractSetting(UUID uuid, SettingKey settingKey, T state) {
        this.uuid = uuid;
        this.settingKey = settingKey;
        this.state = state;
    }

    public abstract void nextState(T currentState);

    public abstract String serialize();
}
