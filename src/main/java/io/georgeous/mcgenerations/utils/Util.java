package io.georgeous.mcgenerations.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Util {

    private static String[] colorCodes = {"§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"};

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

    public static int getRandomInt(int length){
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

    public static ItemStack findInInventory(ItemStack searchItem, PlayerInventory inventory) {
        ItemStack[] stack = inventory.getContents();

        for (ItemStack item : stack) {
            if (item != null) {
                if(item.getType() == searchItem.getType()){
                    return item;
                }
            }
        }
        return null;
    }

    public static String getRandomColor(){
        int i = (int)(Math.random() * colorCodes.length);
        return colorCodes[i];
    }

    public static void giveItemIfNotInInventory(ItemStack item, PlayerInventory inventory) {
        if (Util.findInInventory(item, inventory) == null) {
            inventory.addItem(item);
        }
    }

    public static void setItemsDamage(ItemStack item, float damage) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                if (meta instanceof Damageable) {
                    ((Damageable) meta).setDamage((int) damage);
                    item.setItemMeta(meta);
                }
            }
        }
    }

}
