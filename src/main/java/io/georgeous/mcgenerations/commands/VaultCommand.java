package io.georgeous.mcgenerations.commands;


import io.georgeous.mcgenerations.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class VaultCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("pv")){
            if(!(sender instanceof Player)){
                sender.sendMessage("No!");
                return true;
            }
            Player player = (Player) sender;
            Inventory inv = Bukkit.createInventory(player, 54, player.getName() + "'s Private Vault");

            //if(Main.menus.containsKey(player.getUniqueId().toString()))
              //  inv.setContents(Main.menus.get(player.getUniqueId().toString()));

            player.openInventory(inv);
            return true;
        }
        return false;
    }
}
