package de.taktikcrew.lobbysystem.jumpandrun;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.smoofy.core.api.player.ICorePlayer;
import de.smoofy.core.api.utils.Pair;
import de.taktikcrew.lobbysystem.Lobby;
import de.taktikcrew.lobbysystem.jumpandrun.events.JumpAndRunEnterEvent;
import de.taktikcrew.lobbysystem.jumpandrun.events.JumpAndRunFinishEvent;
import de.taktikcrew.lobbysystem.jumpandrun.listener.JumpAndRunEnterListener;
import de.taktikcrew.lobbysystem.jumpandrun.listener.JumpAndRunFinishListener;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@Accessors(fluent = true)
public class JumpAndRunManager {

    private final Lobby lobby;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Component prefix;

    private final JumpAndRunDAO jumpAndRunDAO;

    private String name = null;
    private String builder = null;
    private JumpAndRun.Difficulty difficulty = null;
    private Location startLocation = null;
    private Location endLocation = null;
    private List<Location> checkpoints = Lists.newArrayList();

    private final Map<String, JumpAndRun> jumpAndRuns;
    private final Map<ICorePlayer, JumpAndRunData> jumpAndRunData;

    private Pair<ICorePlayer, JumpAndRun.SetupStep> setupSteps;

    public JumpAndRunManager(Lobby lobby) {
        this.lobby = lobby;

        this.prefix = this.miniMessage.deserialize("<dark_gray>[<dark_green>JAR<dark_gray>] ");

        this.jumpAndRunDAO = this.lobby.databaseProvider().jumpAndRunDAO();

        new JumpAndRunCommand(this);
        new JumpAndRunListener(this);
        new JumpAndRunEnterListener(this);
        new JumpAndRunFinishListener(this);

        this.jumpAndRuns = Maps.newHashMap();
        this.jumpAndRunData = Maps.newHashMap();
        this.setupSteps = new Pair<>(null, null);

        this.loadJumpAndRuns();
    }

    public boolean isInJumpAndRun(ICorePlayer corePlayer) {
        return this.jumpAndRunData.containsKey(corePlayer);
    }

    public void enterJumpAndRun(ICorePlayer corePlayer, JumpAndRun jumpAndRun) {
        if (this.isInJumpAndRun(corePlayer)) {
            return;
        }
        this.jumpAndRunData.put(corePlayer, new JumpAndRunData(jumpAndRun));
        this.jumpAndRunData.get(corePlayer).checkpoint(this.deserialize(jumpAndRun.startLocation()));

        this.lobby.getServer().getPluginManager().callEvent(new JumpAndRunEnterEvent(corePlayer, jumpAndRun));
    }

    public void abortJumpAndRun(ICorePlayer corePlayer) {
        if (!this.isInJumpAndRun(corePlayer)) {
            return;
        }
        var jumpAndRunData = this.jumpAndRunData.remove(corePlayer);

        this.lobby.getServer().getPluginManager().callEvent(new JumpAndRunFinishEvent(corePlayer, jumpAndRunData, true));
    }

    public void finishJumpAndRun(ICorePlayer corePlayer) {
        if (!this.isInJumpAndRun(corePlayer)) {
            return;
        }
        var jumpAndRunData = this.jumpAndRunData.remove(corePlayer);

        this.lobby.getServer().getPluginManager().callEvent(new JumpAndRunFinishEvent(corePlayer, jumpAndRunData, false));
    }

    public Optional<JumpAndRun> jumpAndRun(String name) {
        return Optional.ofNullable(this.jumpAndRuns.get(name));
    }

    public Optional<JumpAndRun> jumpAndRun(Location startLocation) {
        for (var jumpAndRun : this.jumpAndRuns.values()) {
            if (this.deserialize(jumpAndRun.startLocation()).getBlock().getLocation().equals(startLocation.getBlock().getLocation())) {
                return Optional.of(jumpAndRun);
            }
        }
        return Optional.empty();
    }

    public void sendDifficulties(@NotNull ICorePlayer corePlayer) {
        corePlayer.message(this.prefix.append(Component.translatable("jar.command.setup.difficulty")));
        for (var difficulty : JumpAndRun.Difficulty.values()) {
            corePlayer.message(this.miniMessage.deserialize(" <dark_gray>» ")
                    .append(Component.text(difficulty.name(), difficulty.color()))
                    .append(this.miniMessage.deserialize("<dark_gray>("))
                    .append(Component.text(difficulty.id(), difficulty.color()))
                    .append(this.miniMessage.deserialize("<dark_gray>)")));
        }
    }

    public boolean notInSetup(@NotNull ICorePlayer corePlayer) {
        return !corePlayer.equals(this.setupSteps.key());
    }

    public Location deserialize(@NotNull String location) {
        String[] split = location.split(";");
        return new Location(Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                0);
    }

    private void loadJumpAndRuns() {
        this.jumpAndRunDAO.getAll().forEach(jumpAndRun -> this.jumpAndRuns.put(jumpAndRun.name(), jumpAndRun));
    }
}
