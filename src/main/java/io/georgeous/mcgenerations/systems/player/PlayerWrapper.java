package io.georgeous.mcgenerations.systems.player;

import org.bukkit.entity.Player;

public class PlayerWrapper {
    private final Player player;
    private double karma = 12345;
    private int lives = 0;
    private long playTime = 0;
    private long lastOfflineTime = 0;
    private long timeOfJoin;
    private boolean debugMode = false;

    public PlayerWrapper(Player player) {
        this.player = player;
        this.timeOfJoin = System.currentTimeMillis();
    }

    public int getLives() {
        return this.lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
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

    public void setLastOfflineTime(long lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public long getLastOfflineTime() {
        return lastOfflineTime;
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
}