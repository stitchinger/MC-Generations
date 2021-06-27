package io.georgeous.mcgenerations.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Util {
    // Map Function
    public static final float map(float value,
                                  float istart,
                                  float istop,
                                  float ostart,
                                  float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    public static Player findClosestPlayer(Player player, Double range) {
        Player closest = null;

        double closestDist = 99999;
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.getUniqueId().equals(player.getUniqueId())) { // Not Self
                double dist = player.getLocation().distance(other.getLocation());
                if (dist < range) { // In range
                    if (dist < closestDist) { // Nearest
                        closest = other;
                        closestDist = dist;
                    }
                }
            }
        }
        return closest;
    }


    public static int getRandom(int length){
        int rnd = new Random().nextInt(length);
        return rnd;
    }

    public static ItemStack findInInventory(String name, PlayerInventory inventory) {

        ItemStack[] stack = inventory.getContents();

        for (ItemStack item : stack) {
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getDisplayName().contains(name)) {
                    return item;
                }
            }
        }
        return null;
    }
}
