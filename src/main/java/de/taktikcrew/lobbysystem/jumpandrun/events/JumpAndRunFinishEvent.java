package de.taktikcrew.lobbysystem.jumpandrun.events;

import de.smoofy.core.api.event.BukkitEvent;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRunData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class JumpAndRunFinishEvent extends BukkitEvent {

    private final ICorePlayer corePlayer;
    private final JumpAndRunData jumpAndRunData;
    private final boolean aborted;

}
