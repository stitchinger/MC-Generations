package io.georgeous.mcgenerations.systems.role.lifephase;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.surrogate.SurroManager;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.mcgenerations.utils.Skin;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LifePhase {

    protected final PlayerRole playerRole;
    protected final String name;
    protected int startAge;
    protected int endAge;
    protected final String skinID;
    protected final Skin skin;
    protected int maxCharsInChat;
    protected int hungerRate;
    protected boolean canCarry;
    protected boolean canBeCarried;
    protected boolean surrogate;
    protected boolean feedable;
    protected final List<PotionEffect> effects = new ArrayList<>();
    public static final int BABY_PHASE = 0,
            TODDLER_PHASE = 1,
            CHILD_PHASE = 2,
            TEEN_PHASE = 3,
            ADULT_PHASE = 4,
            ELDER_PHASE = 5;

    public LifePhase(PlayerRole playerRole, int startAge, int endAge, int hungerRate, int maxCharsInChat, boolean canCarry, boolean canBeCarried, String skinID, String name, int jump, int slowness, int digging, boolean surrogate, Skin skin, boolean feedable) {
        this.playerRole = playerRole;
        this.startAge = startAge;
        this.endAge = endAge;
        this.hungerRate = hungerRate;
        this.maxCharsInChat = maxCharsInChat;
        this.canCarry = canCarry;
        this.canBeCarried = canBeCarried;
        this.skinID = skinID;
        this.skin = skin;
        this.name = name;
        this.surrogate = surrogate;
        this.feedable = feedable;

        if (slowness > 0) {
            effects.add(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, slowness, false, false, true));
        }

        if (digging > 0) {
            effects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, digging, false, false, true));
        }

        if (jump > 0) {
            effects.add(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, jump, false, false, true));
        }
    }

    public int getStartAge() {
        return startAge;
    }

    public int getEndAge() {
        return endAge;
    }

    public void applyPotionEffects(Player player, List<PotionEffect> effects) {
        if (effects == null) {
            return;
        }
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }
    }

    public void start() {
        Notification.neutralMsg(playerRole.getPlayer(), "You are a §a" + name);

        Player player = playerRole.getPlayer();
        NickAPI.setSkin(player, skin.value, skin.signature);
        NickAPI.refreshPlayer(player);
        playerRole.refreshHealthBar();

        if (surrogate) {
            SurroManager.create(playerRole.getPlayer(), playerRole.getName() + " " + playerRole.family.getColoredName());
        }
    }

    public void end() {
        if (surrogate) {
            SurroManager.destroy(playerRole.getPlayer());
        }

        playerRole.getPlayer().removePotionEffect(PotionEffectType.SLOW);
        playerRole.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        playerRole.getPlayer().removePotionEffect(PotionEffectType.JUMP);
    }

    public void update() {
        applyPotionEffects(playerRole.getPlayer(), effects);

        if (Math.random() < hungerRate / 100d) {
            int foodLevel = playerRole.getPlayer().getFoodLevel();
            if (foodLevel > 0) {
                playerRole.getPlayer().setFoodLevel(foodLevel - 1);
            }
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

    public String getName() {
        return name;
    }

    public boolean isFeedable() {
        return feedable;
    }
}