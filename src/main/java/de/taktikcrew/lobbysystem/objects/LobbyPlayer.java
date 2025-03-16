package de.taktikcrew.lobbysystem.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
public class LobbyPlayer {

    private final UUID uuid;
    private boolean dsgvoAccepted;

}
