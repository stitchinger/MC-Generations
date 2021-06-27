package io.georgeous.mcgenerations.player;

import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class AgeManager {
    // TODO: Rename AgeControler?
    PlayerWrapper playerWrapper;
    Player player;

    public int ageInYears;
    public long ageInSeconds;
    private int secInYear = 60;
    private long lastTime;

    public AgeManager(PlayerWrapper cp){
        this.playerWrapper = cp;
        this.player = cp.player;
        this.lastTime = System.currentTimeMillis() / 1000;
    }

    public void update(){
        age();
    }

    public void age() {

        long time = System.currentTimeMillis() / 1000;
        long timeSinceLastUpdate = time - lastTime;
        ageInSeconds += timeSinceLastUpdate;

        if (ageInSeconds >= secInYear) {
            this.ageUp();
        }
        lastTime = time;
    }

    public void ageUp() {
        ageInSeconds = 0;
        ageInYears += 1;
        //this.player.sendMessage("Happy Birthday, " + customPlayer.firstName + "! You are §a" + this.ageInYears + "§f!");
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1);
        this.player.setLevel(ageInYears);
    }

    public int getAge(){
        return this.ageInYears;
    }

    public void setAge(int age) {
        ageInYears = age;
    }

    public void setSecInYear(int s){
        secInYear = s;
    }


}
