package io.georgeous.mcgenerations.systems.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerWrapper {
    private final Player player;
    private double karma = 12345;
    private int lifes = 0;
    private long playTime = 0;
    private long lastOfflineTime = 0;
    private long timeOfJoin;
    private boolean debugMode = false;
    private boolean diedOfOldAge = false;
    private Location lastBedLocation = null;

    public PlayerWrapper(Player player) {
        this.player = player;
        this.timeOfJoin = System.currentTimeMillis();
    }

    public int getLifes() {
        return this.lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public void addLife(){
        this.lifes++;
    }

    public double getKarma() {
        return this.karma;
    }

    public void setKarma(double karma) {
        this.karma = karma;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public long getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(long lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public Player getPlayer() {
        return player;
    }

    public long getTimeOfJoin() {
        return timeOfJoin;
    }

    public void setTimeOfJoin(long timeOfJoin) {
        this.timeOfJoin = timeOfJoin;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public boolean getDiedOfOldAge() {
        return diedOfOldAge;
    }

    public void setDiedOfOldAge(boolean diedOfOldAge) {
        this.diedOfOldAge = diedOfOldAge;
    }

    public Location getLastBedLocation() {
        return lastBedLocation;
    }

    public void setLastBedLocation(Location loc) {
        this.lastBedLocation = loc;
    }
}