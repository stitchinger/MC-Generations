package io.georgeous.mcgenerations.player;

import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class MotherController {
    private final PlayerRole playerRole;
    private long lastChildTime;
    public Player child = null;
    public List<Player> children;

    public MotherController(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public void update(){
        childUpdate();
    }

    public boolean canHaveBaby() {
        if (
                playerRole.am.ageInYears > 16 &&
                        System.currentTimeMillis() - lastChildTime > 300000
        ) {
            return true;
        }
        return false;
    }

    public void childUpdate() {
        // Shows childs foodlevel on baby-handler
        if (this.child != null) { // has child?
            if (this.child.getHealth() <= 0) {
                this.child = null;
            }
            updateBabyHandlerDamage();
        }
    }

    public void updateBabyHandlerDamage() {
        PlayerInventory inventory = playerRole.player.getInventory();
        float damage =
                Util.map((float) this.child.getFoodLevel(),
                        0,
                        20f,
                        25,
                        0);

        ItemStack babyHandler = Util.findInInventory("Baby-Handler", inventory);
        playerRole.setItemsDamage(babyHandler,damage);
    }

    public Player getOldestChild() {
        if (children == null)
            return null;

        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) != null) {
                return children.get(i);
            }
        }
        return null;
    }

}