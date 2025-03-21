package de.taktikcrew.lobbysystem.minigames;

import de.smoofy.core.api.player.ICorePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Location;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class DummyCorePlayer implements ICorePlayer {

    @Override
    public String name() {
        return "Bot";
    }

    @Override
    public UUID uuid() {
        return UUID.randomUUID();
    }

    @Override
    public Optional<User> user() {
        return Optional.empty();
    }

    @Override
    public Optional<Group> group() {
        return Optional.empty();
    }

    @Override
    public Component displayName() {
        return Component.text("Bot", NamedTextColor.GREEN);
    }

    @Override
    public NamedTextColor color() {
        return NamedTextColor.GREEN;
    }

    @Override
    public PlayerInventory inventory() {
        return null;
    }

    @Override
    public Location location() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public int coins() {
        return 0;
    }

    @Override
    public void coins(int coins) {

    }

    @Override
    public int onlineTime() {
        return 0;
    }

    @Override
    public void onlineTime(int onlineTime) {

    }

    @Override
    public int playTime() {
        return 0;
    }

    @Override
    public void playTime(int playTime) {

    }

    @Override
    public int nickState() {
        return 0;
    }

    @Override
    public void nickState(int nickState) {

    }

    @Override
    public void message(Component message) {

    }

    @Override
    public void pMessage(Component message) {

    }

    @Override
    public void usage(String usage) {

    }

    @Override
    public void usage(Component prefix, String usage) {

    }

    @Override
    public void noPerms() {

    }

    @Override
    public void notOnline(String target) {

    }

    @Override
    public void sendActionBar(Component message) {

    }

    @Override
    public void sendActionBarPermanent(Component message) {

    }

    @Override
    public void setTablist(boolean minigame) {

    }

    @Override
    public void sendTitle(@NotNull Component title, @NotNull Component subtitle, int fadeIn, int stay, int fadeOut) {

    }

    @Override
    public void resetTitle() {

    }
}
