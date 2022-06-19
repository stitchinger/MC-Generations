package io.georgeous.mcgenerations.systems.player;

import org.bukkit.GameMode;
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
    private boolean isSpawing = false;
    private GameMode lastGameMode = GameMode.SURVIVAL;
    private boolean rulesRead = false;
    private boolean rulesAccepted = false;
    private String namePreference = "random";

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

    public boolean getIsSpawning(){
        return isSpawing;
    }

    public void setIsSpawning(boolean value){
        isSpawing = value;
    }

    public GameMode getLastGameMode(){
        return lastGameMode;
    }

    public void setLastGameMode(GameMode gm){
        lastGameMode = gm;
    }

    public boolean getRulesRead(){
        return rulesRead;
    }

    public void setRulesRead(boolean value){
        rulesRead = value;
    }

    public boolean getRulesAccepted(){
        return rulesAccepted;
    }

    public void setRulesAccepted(boolean value){
        rulesAccepted = value;
    }

    public String getNamePreference(){
        return namePreference;
    }

    public void setNamePreference(String newPref){
        this.namePreference = newPref;
    }
}