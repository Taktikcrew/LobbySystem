package de.taktikcrew.lobbysystem.settings.meta;

import de.taktikcrew.lobbysystem.settings.AbstractSetting;
import de.taktikcrew.lobbysystem.settings.playerhider.PlayerHideState;
import de.taktikcrew.lobbysystem.settings.playerhider.PlayerHider;

import java.util.UUID;

public class SettingFactory {

    public static AbstractSetting<?> settingOfKey(UUID uuid, String key, String state) {
        return switch (SettingRegistry.settingType(key)) {
            case PLAYER_HIDER -> new PlayerHider(uuid, PlayerHideState.valueOf(state));
            default -> null;
        };
    }

}
