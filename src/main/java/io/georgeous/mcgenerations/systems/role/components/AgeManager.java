package io.georgeous.mcgenerations.systems.role.components;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import org.bukkit.Sound;

public class AgeManager {
    PlayerRole playerRole;

    private int ageInYears = 0;
    private long ageInSeconds = 0;
    private int secInYear = 60;
    private long lastTime;

    public AgeManager(PlayerRole playerRole, int startAge) {
        this.playerRole = playerRole;
        this.lastTime = System.currentTimeMillis() / 1000;

        setAge(startAge);
    }

    public void update() {
        long time = System.currentTimeMillis() / 1000;
        long timeSinceLastUpdate = time - lastTime;
        ageInSeconds += timeSinceLastUpdate;

        boolean playerInDebug = PlayerManager.get(playerRole.getPlayer()).isDebugMode();
        if (ageInSeconds >= secInYear && !playerInDebug) {
            this.ageUp();
            ageInSeconds = 0;
        }
        lastTime = time;
    }

    public void ageUp() {
        setAge(ageInYears + 1);
    }

    public int getAge() {
        return ageInYears;
    }

    public void setAge(int age) {
        ageInYears = age;
        //playerRole.getPlayer().setLevel(ageInYears);

        playerRole.getPlayer().playSound(playerRole.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1);
    }

    public void setSecInYear(int s) {
        secInYear = s;
    }
}