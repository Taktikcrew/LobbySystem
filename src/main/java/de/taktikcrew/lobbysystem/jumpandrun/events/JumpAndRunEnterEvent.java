package de.taktikcrew.lobbysystem.jumpandrun.events;

import de.smoofy.core.api.event.BukkitEvent;
import de.smoofy.core.api.player.ICorePlayer;
import de.taktikcrew.lobbysystem.jumpandrun.JumpAndRun;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class JumpAndRunEnterEvent extends BukkitEvent {

    private final ICorePlayer corePlayer;
    private final JumpAndRun jumpAndRun;

}
