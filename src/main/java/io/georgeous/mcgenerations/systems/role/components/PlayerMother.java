package io.georgeous.mcgenerations.systems.role.components;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class PlayerMother {
    private final PlayerRole playerRole;
    private final long BABY_COOLDOWN = 180; // in seconds
    private final int MIN_BIRTH_AGE = 16;
    private final int MAX_BIRTH_AGE = 45;
    private final List<PlayerRole> children;
    private long lastChildTime;
    private boolean isReservedForBaby = false;

    public PlayerMother(PlayerRole playerRole) {
        this.playerRole = playerRole;
        lastChildTime = 0;
        children = new ArrayList<>();
    }

    // Children
    public List<PlayerRole> getChildren() {
        return children;
    }

    public void bornBaby(PlayerRole child) {
        child.getPlayer().teleport(playerRole.getPlayer().getLocation().add(0, 0, 0));
        children.add(child);
        // Inherit from mother
        child.generation = playerRole.generation + 1;
        lastChildTime = System.currentTimeMillis();
        Notification.neutralMsg(playerRole.getPlayer(), "You just had a baby. Congrats");
        Notification.neutralMsg(playerRole.getPlayer(), "You can rename your baby with [/you are Lisa] while carrying it");
        setReservedForBaby(false);
    }

    public boolean canHaveBaby() {
        boolean playerInDebug = PlayerManager.getInstance().getWrapper(playerRole.getPlayer()).isDebugMode();
        return playerRole.getAgeManager().getAge() > MIN_BIRTH_AGE
                && playerRole.getAgeManager().getAge() < MAX_BIRTH_AGE
                && secSinceLastBaby() > BABY_COOLDOWN
                && playerRole.getPlayer().getHealth() > 0
                && !isReservedForBaby()
                && !playerInDebug;
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

    public boolean isOwnChild(PlayerRole childPlayerRole){
        return children.contains(childPlayerRole);
    }

    public boolean isReservedForBaby() {
        return isReservedForBaby;
    }

    public void setReservedForBaby(boolean reservedForBaby) {
        isReservedForBaby = reservedForBaby;
    }
}