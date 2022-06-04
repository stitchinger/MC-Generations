package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PlayerCraftListener implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        PlayerRole playerRole = RoleManager.get().get(player);

        if(playerRole == null){
            return;
        }

        if(event.getSlotType() != InventoryType.SlotType.RESULT){
            return;
        }

        //Bukkit.broadcastMessage(event.getCurrentItem().getData().toString());

        ItemStack item = event.getCurrentItem();

        if(!shouldAddLore(item.getType())){
            return;
        }

        ItemMeta meta = item.getItemMeta();
        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add("");
        itemLore.add(ChatColor.GRAY + "Made by " + ChatColor.WHITE + "" +  playerRole.getName() + " " + playerRole.getFamily().getColoredName());
        itemLore.add(ChatColor.GRAY + "in " + MCG.serverYear);


        meta.setLore(itemLore);
        item.setItemMeta(meta);

    }

    private boolean shouldAddLore(Material itemMaterial){
        Material[] mats = {
                Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL,
                Material.STONE_SWORD, Material.STONE_AXE, Material.STONE_HOE, Material.STONE_PICKAXE, Material.STONE_SHOVEL,
                Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_HOE, Material.IRON_PICKAXE, Material.IRON_SHOVEL,
                Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL,
                Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
                Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
                Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
                Material.BOW, Material.CROSSBOW, Material.SHIELD, Material.SHEARS
        };

        boolean result = false;

        for (Material mat : mats) {
            if(itemMaterial == mat)
                return true;
        }

        return false;
    }
}
