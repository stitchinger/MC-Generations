package io.georgeous.mcgenerations.player.role;

import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class AgeManager {
    PlayerWrapper playerWrapper;
    Player player;

    public int ageInYears = 0;
    public long ageInSeconds = 0;
    private int secInYear = 60;
    private long lastTime;

    public AgeManager(PlayerWrapper cp){
        this.playerWrapper = cp;
        this.player = cp.player;
        this.lastTime = System.currentTimeMillis() / 1000;
    }

    public void update(){
        long time = System.currentTimeMillis() / 1000;
        long timeSinceLastUpdate = time - lastTime;
        ageInSeconds += timeSinceLastUpdate;

        if (ageInSeconds >= secInYear) {
            this.ageUp();
            ageInSeconds = 0;
        }
        lastTime = time;
    }


    public void ageUp() {
        setAge(ageInYears + 1);
    }

    public int getAge(){
        return ageInYears;
    }

    public void setAge(int age) {
        ageInYears = age;
        player.setLevel(ageInYears);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1);
    }

    public void setSecInYear(int s){
        secInYear = s;
    }


}
