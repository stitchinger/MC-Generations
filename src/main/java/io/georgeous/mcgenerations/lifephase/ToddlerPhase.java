package io.georgeous.mcgenerations.lifephase;

import io.georgeous.mcgenerations.manager.SurroManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ToddlerPhase extends LifePhase{

    private Player player;
    private PotionEffect[] effects = {
            new PotionEffect(PotionEffectType.SLOW, 2, 3, false, false, false),
            new PotionEffect(PotionEffectType.HUNGER, 2, 3, false, false, false),
            new PotionEffect(PotionEffectType.SLOW_DIGGING, 2, 3, false, false, false)
    };

    public ToddlerPhase(Player player){
        this.player = player;
        this.skinID = "0";
    }

    @Override
    public void start() {
        player.sendMessage("You are a §atoddler");
        SurroManager.create(player);
    }

    @Override
    public void end() {

    }

    @Override
    public void update() {
        LifePhase.applyPotionEffects(player,effects);
    }
}
