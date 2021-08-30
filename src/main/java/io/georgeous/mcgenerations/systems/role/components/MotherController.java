package io.georgeous.mcgenerations.systems.role.components;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class MotherController {
    private final PlayerRole playerRole;
    private long lastChildTime;
    private final long babyCooldown = 60; // in seconds
    private final List<PlayerRole> children;

    public MotherController(PlayerRole playerRole) {
        this.playerRole = playerRole;
        lastChildTime = 0;
        children = new ArrayList<>();
    }

    public void update() {
        // Shows childs foodlevel on baby-handler
        if (getYoungestChild() != null) { // has child?
            updateBabyHandlerDamage();
        }
    }

    // Children
    public List<PlayerRole> getChildren() {
        return children;
    }

    public void bornBaby(PlayerRole child) {
        child.getPlayer().teleport(playerRole.getPlayer().getLocation().add(0, 1, 0));
        children.add(child);
        // Inherit from mother
        child.generation = playerRole.generation + 1;
        lastChildTime = System.currentTimeMillis();
        Notification.neutralMsg(playerRole.getPlayer(), "You just had a baby. Congrats");
        Notification.neutralMsg(playerRole.getPlayer(), "You can rename your baby with [/you are Lisa] while holding it");
    }

    public boolean canHaveBaby() {
        return playerRole.am.getAge() > 16 &&
                playerRole.am.getAge() < 9999999 &&
                secSinceLastBaby() > babyCooldown;
    }

    private long secSinceLastBaby() {
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
        List<PlayerRole> children = getChildren();
        if (children == null)
            return null;

        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i) != null) {
                return children.get(i);
            }
        }
        return null;
    }

    // BabyHandler
    private float calcDamageForBabyHandler() {
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
        Util.setItemsDamage(babyHandler, calcDamageForBabyHandler());
    }
}