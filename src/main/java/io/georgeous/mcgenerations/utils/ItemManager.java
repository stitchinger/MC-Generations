package io.georgeous.mcgenerations.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemManager {

    public static ItemStack createBabyHandler() {
        ItemStack item = new ItemStack(Material.SHULKER_SHELL);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Baby-Handler");
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "-Right-click baby to feed");
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "-Tap 'Sneak'-button near baby to carry");
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.setCustomModelData(1);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createAnimalIncubator() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Animal-Incubator");
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "1. Right-Click on animal");
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "2. Spawn chosen animal");
            lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "3. Profit");
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

    public static boolean isStarterItem(ItemStack item) {
        if (item == null) {
            return false;
        }

        if(!item.hasItemMeta())
            return false;

        ItemMeta meta = item.getItemMeta();

        if(!meta.hasCustomModelData())
            return false;

        if(meta.getCustomModelData() != 69){
            return false;
        }

        return true;
    }

    public static ItemStack getEveStarterSeeds() {
        // Random Seeds
        ItemStack[] seeds = {
                new ItemStack(Material.CARROT, 16),
                new ItemStack(Material.POTATO, 16),
                new ItemStack(Material.BEETROOT_SEEDS, 32),
                new ItemStack(Material.WHEAT_SEEDS, 32),
                new ItemStack(Material.MELON_SEEDS, 32),
                new ItemStack(Material.PUMPKIN_SEEDS, 32)
        };

        Random r = new Random();
        int randomNumber = r.nextInt(seeds.length);

        ItemStack item = seeds[randomNumber];
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(69);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack getEveStarterFood() {

        ItemStack[] foods = {
                new ItemStack(Material.COOKED_BEEF, 2),
                new ItemStack(Material.COOKED_CHICKEN, 2),
                new ItemStack(Material.COOKED_PORKCHOP, 2),
                new ItemStack(Material.COOKED_MUTTON, 2),
                new ItemStack(Material.COOKED_SALMON, 2),
                new ItemStack(Material.COOKED_COD, 2),
                new ItemStack(Material.COOKED_RABBIT, 2)
        };

        Random r = new Random();
        int randomNumber = r.nextInt(foods.length);

        ItemStack item = foods[randomNumber];
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(69);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack getEveStarterArmor() {
        ItemStack[] armors = {
                new ItemStack(Material.LEATHER_HELMET),
                new ItemStack(Material.LEATHER_CHESTPLATE),
                new ItemStack(Material.LEATHER_LEGGINGS),
                new ItemStack(Material.LEATHER_BOOTS),
        };
        Random r = new Random();
        int randomNumber = r.nextInt(armors.length);

        ItemStack armor = armors[randomNumber];

        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(Util.getRandomColorObject());
        meta.setCustomModelData(69);
        armor.setItemMeta(meta);

        return armor;
    }

}