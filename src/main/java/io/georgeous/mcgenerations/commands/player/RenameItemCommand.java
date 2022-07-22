package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.files.Reporter;
import io.georgeous.mcgenerations.utils.BadWordFilter;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


public class RenameItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only for players");
            return true;
        }

        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /renameitem Excalibur");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType().equals(Material.AIR)){
            Notification.errorMsg(player, "You need to hold an item in your mainhand to rename it");
            return true;
        }


        if(!isNameableItem(item)){
            Notification.errorMsg(player, "You can only rename equipment items");
            return true;
        }


        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return true;
        }


        if(meta.hasDisplayName()){
            Notification.errorMsg(player, "Items can only be named once");
            return true;
        }

        if(!BadWordFilter.getCensoredText(args[0]).equalsIgnoreCase(args[0])){
            Notification.errorMsg(player, "Profanity is not allowed in names!");
            return true;
        }

        meta.setDisplayName(args[0]);
        item.setItemMeta(meta);

        Notification.successMsg(player, "You successfully renamed your item");

        return false;

    }

    private boolean isNameableItem(ItemStack item){
        String[] words = {
                "sword", "shovel", "hoe", "axe", "pickaxe", "shield", "boots", "leggings", "chestplate", "helmet", "bow"
        };

        for (String word : words) {
            if(item.getType().toString().toLowerCase().contains(word))
                return true;
        }
        return false;
    }
}
