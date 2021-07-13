package io.georgeous.mcgenerations.player.wrapper;

import io.georgeous.mcgenerations.player.role.PlayerRole;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class PlayerWrapper {
    public final Player player;
    private double karma = 12345;
    private int lives = 0;
    private long playTime = 0;

    public PlayerWrapper(Player player) {
        this.player = player;
    }

    public int getLives(){
        return this.lives;
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

    public void restoreFrom(ConfigurationSection c){
        int lives = c.getInt("lives");
        setLives(lives);

        double karma = c.getDouble("karma");
        setKarma(karma);

        long playTime = c.getLong("playtime");
        setPlayTime(playTime);
    }
}
