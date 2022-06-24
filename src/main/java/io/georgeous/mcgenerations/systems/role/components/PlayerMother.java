package io.georgeous.mcgenerations.systems.role.components;

import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerMother {
    private final PlayerRole playerRole;
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
        child.setMothersName(playerRole.getName());
        lastChildTime = System.currentTimeMillis();
        Notification.neutralMsg(playerRole.getPlayer(), "You just had a baby. Congrats");
        Notification.neutralMsg(playerRole.getPlayer(), "You can rename your baby with [/you are Lisa] while carrying it");
        setReservedForBaby(false);
    }

    public boolean canHaveBaby() {
        boolean playerInDebug = PlayerManager.get().getWrapper(playerRole.getPlayer()).isDebugMode();
        boolean notTooHungry = playerRole.getPlayer().getFoodLevel() >= 10;
        boolean isHealthy = playerRole.getPlayer().getHealth() >= 4;
        return playerRole.getAgeManager().getAge() > McgConfig.getMinBirthAge()
                && playerRole.getAgeManager().getAge() < McgConfig.getMaxBirthAge()
                && secSinceLastBaby() > McgConfig.getBabyCooldown()
                && playerRole.getPlayer().getHealth() > 0
                && !isReservedForBaby()
                && notTooHungry
                && isHealthy
                && !playerInDebug;
    }

    public void update(){

        if(playerRole.getFamily().getBabyQueue().size() == 0)
            return;

        if(!canHaveBaby())
            return;

        Player baby = playerRole.getFamily().getBabyQueue().get(0);
        SpawnManager.spawnAsBaby(baby, playerRole);
        playerRole.getFamily().getBabyQueue().remove(0);

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