package io.georgeous.mcgenerations.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack createBabyHandler() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Baby-Handler");
            List<String> lore = new ArrayList<String>();
            lore.add("");
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "-Right-click baby to feed");
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "-Sneak near baby to carry");
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "-Shows the babys hunger");
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isBabyHandler(ItemStack item) {
        return isItemByName(item, "Baby-Handler");
    }

    public static boolean isItemByName(ItemStack item, String needle) {
        if (item == null) {
            return false;
        }
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            if (meta.hasDisplayName()) {
                return meta.getDisplayName().contains(needle);
            }
        }
        return false;
    }
}