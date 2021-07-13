package io.georgeous.mcgenerations.player.role.components;

import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class MotherController {
    private final PlayerRole playerRole;
    private long lastChildTime;
    private long babyCooldown = 60; // in seconds
    private List<PlayerRole> children;


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
    public List<PlayerRole> getChildren() {
        return children;
    }

    public void addChild(PlayerRole child){
        children.add(child);
        lastChildTime = System.currentTimeMillis();
    }

    public boolean canHaveBaby() {
        return playerRole.am.getAge() > 16 &&
                playerRole.am.getAge() < 40 &&
                secSinceLastBaby() > babyCooldown;
    }

    private long secSinceLastBaby(){
        return (System.currentTimeMillis() - lastChildTime) / 1000;
    }

    public PlayerRole getOldestChild() {
        if (getChildren() == null)
            return null;

        for (int i = 0; i < getChildren().size(); i++) {
            if (getChildren().get(i) != null) {
                return getChildren().get(i);
            }
        }
        return null;
    }

    public PlayerRole getYoungestChild() {
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
        float foodLevel = (float) getYoungestChild().getPlayer().getFoodLevel();
        return Util.map(foodLevel,
                        0,
                        20f,
                        25,
                        0);
    }

    private void updateBabyHandlerDamage() {
        PlayerInventory inventory = playerRole.getPlayer().getInventory();
        ItemStack babyHandler = Util.findInInventory("Baby-Handler", inventory);
        Util.setItemsDamage(babyHandler,calcDamageForBabyHandler());
    }

}