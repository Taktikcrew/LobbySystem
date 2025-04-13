package de.taktikcrew.lobbysystem;

import de.smoofy.core.api.Core;
import de.taktikcrew.lobbysystem.commands.TestCommand;
import de.taktikcrew.lobbysystem.database.DatabaseProvider;
import de.taktikcrew.lobbysystem.gadgets.GadgetManager;
import de.taktikcrew.lobbysystem.inventories.InventoryProvider;
import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRunManager;
import de.taktikcrew.lobbysystem.listener.block.BlockBreakListener;
import de.taktikcrew.lobbysystem.listener.block.BlockPlaceListener;
import de.taktikcrew.lobbysystem.listener.entity.EntityDamageByEntityListener;
import de.taktikcrew.lobbysystem.listener.entity.EntityPickupItemListener;
import de.taktikcrew.lobbysystem.listener.inventory.InventoryCloseListener;
import de.taktikcrew.lobbysystem.listener.player.*;
import de.taktikcrew.lobbysystem.listener.world.FoodLevelChangeListener;
import de.taktikcrew.lobbysystem.listener.world.WeatherChangeListener;
import de.taktikcrew.lobbysystem.minigames.MiniGameProvider;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

@Getter
@Accessors(fluent = true)
public class Lobby extends JavaPlugin {

    private DatabaseProvider databaseProvider;

    private GadgetManager gadgetManager;
    private JumpAndRunManager jumpAndRunManager;

    private InventoryProvider inventoryProvider;
    private MiniGameProvider miniGameProvider;

    @Override
    public void onEnable() {
        Core.instance().localize().init(Lobby.class, "Lobby", Locale.GERMANY, Locale.US);

        this.databaseProvider = new DatabaseProvider();
        this.databaseProvider.create();

        this.gadgetManager = new GadgetManager(this);
        this.jumpAndRunManager = new JumpAndRunManager(this);

        this.inventoryProvider = new InventoryProvider(this);
        this.miniGameProvider = new MiniGameProvider(this);

        this.registerCommands();
        this.registerListener();
    }

    @Override
    public void onDisable() {
        this.databaseProvider.close();
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("test", new TestCommand(this));
        });
    }

    private void registerListener() {
        new BlockBreakListener(this);
        new BlockPlaceListener(this);

        new EntityDamageByEntityListener(this);
        new PlayerDropItemListener(this);
        new EntityPickupItemListener(this);

        new InventoryCloseListener(this);

        new PlayerGameModeChangeListener(this);
        new PlayerItemConsumeListener(this);
        new PlayerJoinListener(this);
        new PlayerMoveListener(this);
        new PlayerQuitListener(this);
        new PlayerSwapHandItemsListener(this);

        new FoodLevelChangeListener(this);
        new WeatherChangeListener(this);
    }
}
