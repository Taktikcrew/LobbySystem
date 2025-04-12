package de.taktikcrew.lobbysystem.settings.meta;

import com.google.common.collect.Maps;

import java.util.Map;

public class SettingRegistry {

    private static final Map<String, SettingType> types = Maps.newHashMap();

    static {
        types.put("player_hider", SettingType.PLAYER_HIDER);
    }

    public static SettingType settingType(String key) {
        return types.getOrDefault(key, SettingType.STRING);
    }
}
