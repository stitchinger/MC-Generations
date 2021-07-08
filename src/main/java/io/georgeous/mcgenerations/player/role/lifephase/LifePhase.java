package io.georgeous.mcgenerations.player.role.lifephase;

import io.georgeous.mcgenerations.manager.SurroManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class LifePhase {

    protected final Player player;
    protected final String name;
    protected int startAge;
    protected int endAge;
    protected final String skinID;
    protected int maxCharsInChat;
    protected int hungerRate;
    protected boolean canCarry;
    protected boolean canBeCarried;
    protected boolean surrogate;
    protected final List<PotionEffect> effects = new ArrayList<>();
    public static final int BABY_PHASE = 0,
            TODDLER_PHASE = 1,
            CHILD_PHASE = 2,
            TEEN_PHASE = 3,
            ADULT_PHASE = 4,
            ELDER_PHASE = 5;


    public LifePhase(Player player, int startAge, int endAge, int hungerRate, int maxCharsInChat, boolean canCarry, boolean canBeCarried, String skinID, String name, int jump, int slowness, int digging, boolean surrogate){
        this.player = player;
        this.startAge = startAge;
        this.endAge = endAge;
        this.hungerRate = hungerRate;
        this.maxCharsInChat = maxCharsInChat;
        this.canCarry = canCarry;
        this.canBeCarried = canBeCarried;
        this.skinID = skinID;
        this.name = name;
        this.surrogate = surrogate;

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

    public int getStartAge(){
        return startAge;
    }

    public int getEndAge(){
        return endAge;
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
        if(surrogate){
            SurroManager.create(player);
        }
    }

    public void end() {
        if(surrogate){
            SurroManager.destroySurrogate(player);
        }

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

    public String getName(){
        return name;
    }

}
