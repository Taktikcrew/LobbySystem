package de.taktikcrew.lobbysystem.jumpandrun;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Location;

@Getter
@Setter
@Accessors(fluent = true)
public class JumpAndRunData {

    private final JumpAndRun jumpAndRun;
    private int fails;
    private Location checkpoint;
    private long startTime;

    public JumpAndRunData(JumpAndRun jumpAndRun) {
        this.jumpAndRun = jumpAndRun;
        this.fails = 0;
        this.checkpoint = null;
        this.startTime = System.currentTimeMillis();
    }

    public void addFail() {
        this.fails++;
    }
}
