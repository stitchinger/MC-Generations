package io.georgeous.mcgenerations.player.role;

import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class MotherController {
    private final PlayerRole playerRole;
    private long lastChildTime;
    private List<Player> children;

    public MotherController(PlayerRole playerRole) {
        this.playerRole = playerRole;
        lastChildTime = 0;
        children = new ArrayList<>();
    }

    public void update(){
        // Shows childs foodlevel on baby-handler
        if (getYoungestChild() != null) { // has child?
            updateBabyHandlerDamage();
        }
    }

    // Children
    public List<Player> getChildren() {
        return children;
    }

    public void addChild(Player child){
        getChildren().add(child);
        lastChildTime = System.currentTimeMillis();
    }

    public long getLastChildTime() {
        return lastChildTime;
    }

    public boolean canHaveBaby() {
        return playerRole.am.ageInYears > 16 &&
                System.currentTimeMillis() - getLastChildTime() > 300000;
    }

    public Player getOldestChild() {
        if (getChildren() == null)
            return null;

        for (int i = 0; i < getChildren().size(); i++) {
            if (getChildren().get(i) != null) {
                return getChildren().get(i);
            }
        }
        return null;
    }

    public Player getYoungestChild() {
        if (getChildren() == null)
            return null;

        for (int i = getChildren().size()-1; i >= 0; i--) {
            if (getChildren().get(i) != null) {
                return getChildren().get(i);
            }
        }
        return null;
    }

    // BabyHandler
    private float calcDamageForBabyHandler(){
        float foodLevel = (float) getYoungestChild().getFoodLevel();
        return Util.map(foodLevel,
                        0,
                        20f,
                        25,
                        0);
    }

    private void updateBabyHandlerDamage() {
        PlayerInventory inventory = playerRole.player.getInventory();
        ItemStack babyHandler = Util.findInInventory("Baby-Handler", inventory);
        Util.setItemsDamage(babyHandler,calcDamageForBabyHandler());
    }

}