package de.taktikcrew.lobbysystem.settings;

import de.taktikcrew.lobbysystem.settings.meta.SettingKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class AbstractSetting<T> {

    private final UUID uuid;
    private final SettingKey settingKey;
    @Setter
    private T state;

    public abstract void nextState(T currentState);

    public abstract String serialize();
}
