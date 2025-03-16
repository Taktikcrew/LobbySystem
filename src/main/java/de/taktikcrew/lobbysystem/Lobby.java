package de.taktikcrew.lobbysystem;

import de.smoofy.core.api.Core;
import de.taktikcrew.lobbysystem.database.LobbyPlayerDAO;
import de.taktikcrew.lobbysystem.inventories.DsgvoInventory;
import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRunDAO;
import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRunManager;
import de.taktikcrew.lobbysystem.listener.block.BlockBreakListener;
import de.taktikcrew.lobbysystem.listener.block.BlockPlaceListener;
import de.taktikcrew.lobbysystem.listener.entity.EntityDamageByEntityListener;
import de.taktikcrew.lobbysystem.listener.entity.EntityDropItemListener;
import de.taktikcrew.lobbysystem.listener.entity.EntityPickupItemListener;
import de.taktikcrew.lobbysystem.listener.inventory.InventoryCloseListener;
import de.taktikcrew.lobbysystem.listener.player.*;
import de.taktikcrew.lobbysystem.listener.world.FoodLevelChangeListener;
import de.taktikcrew.lobbysystem.listener.world.WeatherChangeListener;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

@Getter
@Accessors(fluent = true)
public class Lobby extends JavaPlugin {

    private JumpAndRunDAO jumpAndRunDAO;
    private LobbyPlayerDAO lobbyPlayerDAO;

    private JumpAndRunManager jumpAndRunManager;

    private DsgvoInventory dsgvoInventory;

    @Override
    public void onEnable() {
        Core.instance().localize().init(Lobby.class, "Lobby", Locale.GERMANY, Locale.US);

        this.jumpAndRunDAO = new JumpAndRunDAO();
        this.lobbyPlayerDAO = new LobbyPlayerDAO();

        this.jumpAndRunManager = new JumpAndRunManager(this);

        this.dsgvoInventory = new DsgvoInventory(this);

        this.registerCommands();
        this.registerListener();
    }

    private void registerCommands() {

    }

    private void registerListener() {
        new BlockBreakListener(this);
        new BlockPlaceListener(this);

        new EntityDamageByEntityListener(this);
        new EntityDropItemListener(this);
        new EntityPickupItemListener(this);

        new InventoryCloseListener(this);

        new PlayerGameModeChangeListener(this);
        new PlayerInteractListener(this);
        new PlayerJoinListener(this);
        new PlayerMoveListener(this);
        new PlayerQuitListener(this);
        new PlayerSwapHandItemsListener(this);
        new PlayerToggleFlightListener(this);

        new FoodLevelChangeListener(this);
        new WeatherChangeListener(this);
    }
}
