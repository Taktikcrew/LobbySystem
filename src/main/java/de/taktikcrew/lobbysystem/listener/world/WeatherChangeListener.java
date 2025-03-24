package de.taktikcrew.lobbysystem.listener.world;

import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.jetbrains.annotations.NotNull;

public class WeatherChangeListener implements Listener {

    public WeatherChangeListener(@NotNull Lobby lobby) {
        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onChange(@NotNull WeatherChangeEvent event) {
        if (!event.toWeatherState()) {
            return;
        }
        event.setCancelled(true);
    }
}