package de.taktikcrew.lobbysystem.settings.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum SettingKey {

    PLAYER_HIDER("player_hider");

    private final String key;
}
