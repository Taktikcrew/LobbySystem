package de.taktikcrew.lobbysystem.utils;

public class Stringify {

    public static String time(long time) {
        int minutes = (int) ((time / 1000) / 60);
        int seconds = (int) ((time / 1000) % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

}
