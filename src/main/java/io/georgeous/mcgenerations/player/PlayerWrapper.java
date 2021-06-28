package io.georgeous.mcgenerations.player;

import org.bukkit.entity.Player;


public class PlayerWrapper {
    public final Player player;
    private PlayerRole playerRole;

    private double karma = 12345;
    private int lives = 0;
    private long playTime = 0;

    public PlayerWrapper(Player player) {
        this.player = player;
        this.setRole(null);
    }

    // Update Functions
    public void update() {
        if(getRole() != null)
            getRole().update();
    }

    public PlayerRole getRole() {
        return playerRole;
    }

    public void setRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
        this.lives++;
    }

    public int getLives(){
        return this.lives;
    }
    public void removeRole(){
        this.setRole(null);
    }

    public void setLives(int lives){
        this.lives = lives;
    }

    public double getKarma(){
        return this.karma;
    }

    public void setKarma(double karma){
        this.karma = karma;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }
}
