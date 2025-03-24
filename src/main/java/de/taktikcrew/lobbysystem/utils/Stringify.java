package de.taktikcrew.lobbysystem.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Stringify {

    @Contract(pure = true)
    public static @NotNull String time(long time) {
        int minutes = (int) ((time / 1000) / 60);
        int seconds = (int) ((time / 1000) % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

}
