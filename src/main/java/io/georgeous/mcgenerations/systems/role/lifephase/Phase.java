package io.georgeous.mcgenerations.systems.role.lifephase;

import io.georgeous.mcgenerations.utils.Skin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public enum Phase {

    BABY(0, 3, 1, .7f, false, true,
            "", "Baby", 0,128, 3, 2, true, Skin.BABY2, true, "Toddler"),
    TODDLER(3, 6, 1, .9f, false, true,
            "", "Toddler", 1, 0, 2, 1, true, Skin.BABY2, true, "Child"),
    CHILD(6, 15, 0, .97f, false, false,
            "2007359867", "Child", 2,0, 1, 0, false, Skin.CHILD2, true, "Teen"),
    TEEN(15, 21, 0, 1, true, false,
            "297371", "Teen", 3,0, 0, 0, false, Skin.TEEN2, false, "Adult"),
    ADULT(21, 40, 0, 1, true, false,
            "584227931", "Adult", 4, 0, 0, 0, false, Skin.ADULT2, false, "Elder"),
    ELDER(40, 60, 0, 1, true, false,
            "1144027445", "Elder", 5, 0, 0, 0, false, Skin.ELDER2, false, null);

    public final String name;
    public final int id;
    public final int startAge;
    public final int endAge;
    public final String skinID;
    public final Skin skin;
    public final float spellAccuracy;
    public final int hungerRate;
    public final boolean canCarry;
    public final boolean canBeCarried;
    public final boolean surrogate;
    public final boolean feedable;
    public final List<PotionEffect> effects = new ArrayList<>();
    public final String nextPhase;

    Phase(int startAge, int endAge, int hungerRate, float spellAccuracy, boolean canCarry, boolean canBeCarried, String skinID,
          String name, int id, int jump, int slowness, int digging, boolean surrogate, Skin skin, boolean feedable, String nextPhase) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.hungerRate = hungerRate;
        this.spellAccuracy = spellAccuracy;
        this.canCarry = canCarry;
        this.canBeCarried = canBeCarried;
        this.skinID = skinID;
        this.skin = skin;
        this.name = name;
        this.id = id;
        this.surrogate = surrogate;
        this.feedable = feedable;
        this.nextPhase = nextPhase;

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

    public boolean canCarry() {
        return canCarry;
    }


    public float getSpellAccuracy() {
        return spellAccuracy;
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

    public int getId() {
        return id;
    }

    public boolean isFeedable() {
        return feedable;
    }
}
