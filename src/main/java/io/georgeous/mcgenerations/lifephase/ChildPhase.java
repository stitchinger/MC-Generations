package io.georgeous.mcgenerations.lifephase;

import io.georgeous.mcgenerations.manager.SurroManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ChildPhase extends LifePhase{

    private Player player;
    private PotionEffect[] effects = {
            new PotionEffect(PotionEffectType.SLOW, 2, 1, false, false, false),
            //new PotionEffect(PotionEffectType.HUNGER, 2, 1, false, false, false),
            new PotionEffect(PotionEffectType.SLOW_DIGGING, 2, 1, false, false, false)
    };

    public ChildPhase(Player player){
        this.player = player;
        this.skinID = "2007359867";
        this.canCarry = false;
        this.maxCharsInChat = 12;
    }

    @Override
    public void start() {
        player.sendMessage("You are a §achild");
        //cp.destroySurrogate();
        //NameManager.changeSkin(this.player, PhaseManager.skinIds[2]);
        SurroManager.destroySurrogate(player);
        this.canCarry = false;
    }

    @Override
    public void end() {

    }

    @Override
    public void update() {
        LifePhase.applyPotionEffects(player,effects);

    }
}
