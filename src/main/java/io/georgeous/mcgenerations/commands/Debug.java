package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.family.FamilyManager;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.manager.SurroManager;

import org.bukkit.Location;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;


public class Debug implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length >= 1) {
            // Council Teleport
            if (args[0].equalsIgnoreCase("council")) { // Council Teleport
                player.teleport(new Location(player.getWorld(), 0, 250, 0));
            }
            // Save Players in config file
            else if (args[0].equalsIgnoreCase("save")) { //
                PlayerManager.saveAllPlayers();
            }
        }

        return false;
    }
}
