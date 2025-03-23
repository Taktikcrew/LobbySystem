package de.taktikcrew.lobbysystem.listener.world;

import de.taktikcrew.lobbysystem.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    public WeatherChangeListener(Lobby lobby) {
        lobby.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onChange(WeatherChangeEvent event) {
        if (!event.toWeatherState()) {
            return;
        }
        event.setCancelled(true);
    }
}