package de.taktikcrew.lobbysystem.settings.playerhider;

import de.smoofy.core.api.builder.ItemBuilder;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.settings.AbstractSetting;
import de.taktikcrew.lobbysystem.settings.meta.SettingKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import java.util.UUID;

public class PlayerHider extends AbstractSetting<PlayerHideState> {

    public PlayerHider(UUID uuid, PlayerHideState state) {
        super(uuid, SettingKey.PLAYER_HIDER, state);
    }

    private ItemStack playerHider(ICorePlayer corePlayer, String itemKey, Color color) {
        var item = ItemBuilder.of(Material.FIREWORK_STAR)
                .name(Component.translatable(itemKey))
                .event("player_hider", PlayerInteractEvent.class, _ -> {
                    switch (this.state()) {
                        case SHOW_ALL -> this.setShowVipItem(corePlayer);
                        case SHOW_VIP -> this.setShowNoneItem(corePlayer);
                        case SHOW_NONE -> this.setShowAllItem(corePlayer);
                    }
                    this.nextState(this.state());
                })
                .build();

        item.editMeta(FireworkEffectMeta.class, fireworkEffectMeta -> {
            fireworkEffectMeta.setEffect(FireworkEffect.builder().withColor(color).build());
            item.setItemMeta(fireworkEffectMeta);
        });

        return item;
    }

    public void setShowAllItem(ICorePlayer corePlayer) {
        corePlayer.inventory().setItem(1, this.playerHider(corePlayer, "lobby.menu.player.item.player_hider_all.name", Color.LIME));
    }

    public void setShowVipItem(ICorePlayer corePlayer) {
        corePlayer.inventory().setItem(1, this.playerHider(corePlayer, "lobby.menu.player.item.player_hider_vip.name", Color.PURPLE));
    }

    public void setShowNoneItem(ICorePlayer corePlayer) {
        corePlayer.inventory().setItem(1, this.playerHider(corePlayer, "lobby.menu.player.item.player_hider_none.name", Color.RED));
    }

    @Override
    public void nextState(PlayerHideState currentState) {
        switch (currentState) {
            case SHOW_ALL -> this.state(PlayerHideState.SHOW_VIP);
            case SHOW_VIP -> this.state(PlayerHideState.SHOW_NONE);
            case SHOW_NONE -> this.state(PlayerHideState.SHOW_ALL);
        }
    }

    @Override
    public String serialize() {
        return this.state().name();
    }
}
