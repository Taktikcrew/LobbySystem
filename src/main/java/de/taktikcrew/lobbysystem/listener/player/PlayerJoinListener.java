package de.taktikcrew.lobbysystem.listener.player;

import de.smoofy.core.api.Core;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.database.LobbyPlayerDAO;
import de.taktikcrew.lobbysystem.objects.LobbyPlayer;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    private final Lobby lobby;
    private final LobbyPlayerDAO lobbyPlayerDAO;

    public PlayerJoinListener(Lobby lobby) {
        this.lobby = lobby;
        this.lobbyPlayerDAO = this.lobby.lobbyPlayerDAO();

        this.lobby.getServer().getPluginManager().registerEvents(this, this.lobby);
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        var player = event.getPlayer();
        var corePlayer = Core.instance().corePlayerProvider().corePlayer(player);

        this.lobbyPlayerDAO.create(new LobbyPlayer(player.getUniqueId(), false));
        var optionalLobbyPlayer = this.lobbyPlayerDAO.get(player.getUniqueId());

        if (optionalLobbyPlayer.isPresent()) {
            var lobbyPlayer = optionalLobbyPlayer.get();

            if (!lobbyPlayer.dsgvoAccepted()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));

                Core.instance().coreTask().later(() -> player.openInventory(this.lobby.inventoryProvider().dsgvoInventory().inventory()), 1);

                return;
            }
        }
        player.clearActivePotionEffects();
        player.setCollidable(false);
        player.setGameMode(GameMode.SURVIVAL);
        if (player.hasPermission("lobby.fly")) {
            player.setAllowFlight(true);
        }
        player.setLevel(0);
        player.setExp(0);

        this.lobby.inventoryProvider().lobbyPlayerInventory().setLobbyInventory(corePlayer);
    }
}