package de.taktikcrew.lobbysystem.jumpandrun;

import dev.httpmarco.evelon.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public class JumpAndRun {

    @PrimaryKey
    private final String name;
    private final String builder;

    private final Difficulty difficulty;

    private final String startLocation;
    private final String endLocation;
    private List<String> checkpoints;

    @Setter
    private long recordTime;

    @Setter
    private boolean playable;

    public JumpAndRun(String name, String builder, Difficulty difficulty, Location startLocation, Location endLocation, List<Location> checkpoints) {
        this.name = name;
        this.builder = builder;
        this.difficulty = difficulty;
        this.startLocation = this.serialize(startLocation);
        this.endLocation = this.serialize(endLocation);
        this.recordTime = Long.MAX_VALUE;
        if (checkpoints != null) {
            this.checkpoints = checkpoints.stream().map(this::serialize).collect(Collectors.toList());
        }
        this.playable = true;
    }

    private String serialize(Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw();
    }

    @Getter
    @Accessors(fluent = true)
    @AllArgsConstructor
    public enum Difficulty {

        FREE(0, NamedTextColor.GRAY),
        EASY(1, NamedTextColor.GREEN),
        NORMAL(2, NamedTextColor.YELLOW),
        HARD(3, NamedTextColor.RED),
        EXTREME(4, NamedTextColor.AQUA);

        private final int id;
        private final NamedTextColor color;

        public static Optional<Difficulty> byId(int id) {
            return Arrays.stream(values()).filter(difficulty -> difficulty.id == id).findFirst();
        }
    }

    public enum SetupStep {
        NAME, BUILDER, DIFFICULTY, START_LOCATION, END_LOCATION, CHECKPOINTS;
    }

}
