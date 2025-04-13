package de.taktikcrew.lobbysystem.inventories;

import de.smoofy.core.api.builder.ItemBuilder;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRunManager;
import de.taktikcrew.lobbysystem.lobbyplayer.LobbyPlayerDAO;
import de.taktikcrew.lobbysystem.settings.meta.SettingKey;
import de.taktikcrew.lobbysystem.settings.playerhider.PlayerHider;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class LobbyPlayerInventory {

    private final LobbyPlayerDAO lobbyPlayerDAO;

    private final JumpAndRunManager jumpAndRunManager;

    public LobbyPlayerInventory(@NotNull InventoryProvider inventoryProvider) {
        this.lobbyPlayerDAO = inventoryProvider.lobby().databaseProvider().lobbyPlayerDAO();

        this.jumpAndRunManager = inventoryProvider.lobby().jumpAndRunManager();
    }

    public void setLobbyInventory(@NotNull ICorePlayer corePlayer, boolean clearInventory) {
        var lobbyPlayer = this.lobbyPlayerDAO.get(corePlayer.uuid());
        if (lobbyPlayer.isEmpty()) {
            return;
        }

        if (clearInventory) {
            corePlayer.inventory().clear();
        }

        corePlayer.inventory().setItem(0, ItemBuilder.of(Material.COMPASS)
                .name(Component.translatable("lobby.menu.player.item.navigator.name"))
                .build());

        var optionalSetting = lobbyPlayer.get().settingByKey(SettingKey.PLAYER_HIDER);
        if (optionalSetting.isEmpty()) {
            return;
        }

        if (!(optionalSetting.get() instanceof PlayerHider playerHider)) {
            return;
        }

        switch (playerHider.state()) {
            case SHOW_ALL -> playerHider.setShowAllItem(corePlayer);
            case SHOW_VIP -> playerHider.setShowVipItem(corePlayer);
            case SHOW_NONE -> playerHider.setShowNoneItem(corePlayer);
        }

        corePlayer.inventory().setItem(7, ItemBuilder.of(Material.CHEST)
                .name(Component.translatable("lobby.menu.player.item.gadgets.name"))
                .build());

        corePlayer.inventory().setItem(8, ItemBuilder.of(Material.PLAYER_HEAD)
                .skullOwner(corePlayer)
                .name(Component.translatable("lobby.menu.player.item.profile.name"))
                .build());

        corePlayer.bukkitPlayer().ifPresent(player -> {
            if (!player.hasPermission("lobby.nick")) {
                return;
            }

            corePlayer.inventory().setItem(3, ItemBuilder.of(Material.NAME_TAG)
                    .name(Component.translatable("lobby.menu.player.item.nick.name"))
                    .build());

            if (!player.hasPermission("lobby.vip")) {
                return;
            }

            corePlayer.inventory().setItem(5, ItemBuilder.of(Material.TNT)
                    .name(Component.translatable("lobby.menu.player.item.silent_hub.name"))
                    .build());
        });
    }

    public void setJumpAndRunInventory(ICorePlayer corePlayer) {
        corePlayer.inventory().clear();

        corePlayer.inventory().setItem(0, ItemBuilder.of(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .name(Component.translatable("lobby.jar.item.checkpoint.name"))
                .event("jar_back_to_checkpoint", PlayerInteractEvent.class, _ -> {
                    var jumpAndRunData = this.jumpAndRunManager.jumpAndRunData().get(corePlayer);
                    jumpAndRunData.addFail();
                    corePlayer.bukkitPlayer().ifPresent(player -> player.teleport(jumpAndRunData.checkpoint()));
                })
                .build());

        corePlayer.inventory().setItem(8, ItemBuilder.of(Material.BARRIER)
                .name(Component.translatable("lobby.jar.item.abort.name"))
                .event("jar_abort", PlayerInteractEvent.class, _ ->
                        this.jumpAndRunManager.abortJumpAndRun(corePlayer)
                )
                .build());
    }
}
