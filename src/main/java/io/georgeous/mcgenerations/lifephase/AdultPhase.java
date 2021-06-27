package io.georgeous.mcgenerations.lifephase;

import io.georgeous.mcgenerations.manager.SurroManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AdultPhase extends LifePhase{

    private Player player;

    public AdultPhase(Player player){
        this.player = player;
        this.skinID = "584227931";
    }

    @Override
    public void start() {
        player.sendMessage("You are an §aadult");
        //cp.destroySurrogate();
        //NameManager.changeSkin(this.player, PhaseManager.skinIds[4]);
        SurroManager.destroySurrogate(player);
    }

    @Override
    public void end() {

    }

    @Override
    public void update() {
        PotionEffect mothersLoveSpeed = new PotionEffect(PotionEffectType.SPEED, 2, 1, false, false, true);
        player.addPotionEffect(mothersLoveSpeed);

        PotionEffect mothersLoveDigging = new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 1, false, false, true);
        player.addPotionEffect(mothersLoveDigging);
    }
}
