package io.georgeous.mcgenerations.lifephase;

import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.manager.SurroManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ElderPhase extends LifePhase{

    private Player player;
    private PotionEffect[] effects = {
            new PotionEffect(PotionEffectType.SLOW, 2, 1, false, false, false),
            //new PotionEffect(PotionEffectType.HUNGER, 2, 3, false, false, false),
            //new PotionEffect(PotionEffectType.SLOW_DIGGING, 2, 3, false, false, false)
    };

    public ElderPhase(Player player){
        this.player = player;
        this.skinID = "1144027445";
    }

    @Override
    public void start() {
        player.sendMessage("You are an §aelder");
        //NameManager.changeSkin(this.player, PhaseManager.skinIds[5]);
        SurroManager.destroySurrogate(player);

        PlayerWrapper cp = PlayerManager.get(player);
        if(Util.findInInventory("Baby-Handler", cp.player.getInventory()) != null){
            Util.findInInventory("Baby-Handler", cp.player.getInventory()).setAmount(0);
        }
    }

    @Override
    public void end() {

    }

    @Override
    public void update() {
        LifePhase.applyPotionEffects(player,effects);

    }
}
