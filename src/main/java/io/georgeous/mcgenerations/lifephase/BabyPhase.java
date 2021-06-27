package io.georgeous.mcgenerations.lifephase;

import io.georgeous.mcgenerations.manager.SurroManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BabyPhase extends LifePhase {

    private Player player;
    private double hungerRate = 1d;
    private PotionEffect[] effects = {
            new PotionEffect(PotionEffectType.SLOW, 2, 6, false, false, false),
            //new PotionEffect(PotionEffectType.HUNGER, 2, 5, false, false, false),
            new PotionEffect(PotionEffectType.SLOW_DIGGING, 2, 5, false, false, false),
            new PotionEffect(PotionEffectType.JUMP, 2, 128, false, false, false)
    };

    public BabyPhase(Player player) {
        this.player = player;
        this.skinID = "0";
    }

    @Override
    public void start() {
        player.sendMessage("You are a §ababy");
        player.getInventory().clear();
        SurroManager.create(player);
    }

    @Override
    public void end() {
        for (PotionEffect p : player.getActivePotionEffects()) {
            player.removePotionEffect(p.getType());
        }
    }

    @Override
    public void update() {
        LifePhase.applyPotionEffects(player, effects);

        if(Math.random() < hungerRate / 100d){
            player.setFoodLevel(player.getFoodLevel()-1);
        }

        player.setSaturation(0);
    }
}
