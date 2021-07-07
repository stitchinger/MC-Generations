package io.georgeous.mcgenerations.lifephase;

import io.georgeous.mcgenerations.manager.SurroManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class LifePhaseNew {

    private final Player player;
    private final String name;
    private final String skinID;
    private int maxCharsInChat = 999;
    private int hungerRate = 0;
    private boolean canCarry = true;
    private boolean canBeCarried = false;
    private final List<PotionEffect> effects = new ArrayList<>();


    public LifePhaseNew(Player player, int hungerRate, int maxCharsInChat, boolean canCarry, boolean canBeCarried, String skinID, String name, int jump, int slowness, int digging){
        this.player = player;
        this.hungerRate = hungerRate;
        this.maxCharsInChat = maxCharsInChat;
        this.canCarry = canCarry;
        this.canBeCarried = canBeCarried;
        this.skinID = skinID;
        this.name = name;

        if(slowness > 0){
            effects.add(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, slowness, false, false, false));
        }

        if(digging > 0){
            effects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, digging, false, false, false));
        }

        if(jump > 0){
            effects.add(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, jump, false, false, false));
        }
    }

    public void applyPotionEffects(Player player, List<PotionEffect> effects){
        if(effects == null){
            return;
        }
        for(PotionEffect effect : effects){
            player.addPotionEffect(effect);
        }
    }

    public void start() {
        player.sendMessage("You are a §a"+ name);
        //player.setWalkSpeed(0.2f);

        //player.getInventory().clear();
        //SurroManager.create(player);
    }


    public void end() {
        for (PotionEffect p : player.getActivePotionEffects()) {
            player.removePotionEffect(p.getType());
        }
    }

    public void update() {
        applyPotionEffects(player, effects);

        if(Math.random() < hungerRate / 100d){
            player.setFoodLevel(player.getFoodLevel()-1);
        }
    }

    public boolean canCarry() {
        return canCarry;
    }

    public int getMaxCharsInChat() {
        return maxCharsInChat;
    }

    public String getSkinID() {
        return skinID;
    }

    public boolean canBeCarried() {
        return canBeCarried;
    }

}
