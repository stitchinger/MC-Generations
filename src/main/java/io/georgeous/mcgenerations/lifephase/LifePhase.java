package io.georgeous.mcgenerations.lifephase;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public abstract class LifePhase {

    public String skinID;
    public int maxWords = 1;

    public static final int BABY_PHASE = 0,
            TODDLER_PHASE = 1,
            CHILD_PHASE = 2,
            TEEN_PHASE = 3,
            ADULT_PHASE = 4,
            ELDER_PHASE = 5;

    public abstract void start();
    public abstract  void end();
    public abstract void update();

    public static void applyPotionEffects(Player player, PotionEffect[] effects){
        for(PotionEffect effect : effects){
            player.addPotionEffect(effect);
        }
    }
}
