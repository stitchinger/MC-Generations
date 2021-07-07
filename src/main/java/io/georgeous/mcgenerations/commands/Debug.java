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
        if(!(sender instanceof Player)){
            return true;
        }

        Player player = (Player) sender;

        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("surrogate")){
                if(SurroManager.map.get(player) != null){
                    SurroManager.destroySurrogate(player);
                }else{
                    SurroManager.create(player);
                }

            } else if(args[0].equalsIgnoreCase("pets")){


            } else if(args[0].equalsIgnoreCase("banner")){ // Banner
                ItemStack hand = player.getInventory().getItemInMainHand();
                //p.sendMessage(hand.toString());
                if(hand.getItemMeta() instanceof BannerMeta){
                    player.sendMessage(((BannerMeta) hand.getItemMeta()).getPatterns().toString());
                    for(Pattern pat : ((BannerMeta) hand.getItemMeta()).getPatterns()){
                        pat.getPattern().equals(PatternType.CREEPER);
                    }
                }
            } else if(args[0].equalsIgnoreCase("council")){ // Council Teleport
                player.teleport(new Location(player.getWorld(),0,250,0));

            }
            else if(args[0].equalsIgnoreCase("exp")){ // Council Teleport


            }
            else if(args[0].equalsIgnoreCase("save")){ //
                PlayerManager.saveAllPlayers();
            }
            else if(args[0].equalsIgnoreCase("load")){ //
                PlayerManager.restoreAllPlayers();
            }
            else if(args[0].equalsIgnoreCase("savefamily")){ //
                //FamilyManager.saveAllFamilies();
            }
            else if(args[0].equalsIgnoreCase("loadfamily")){ //
                //PlayerManager.restorePlayer(PlayerManager.get(player));
                //PlayerManager.restoreAllPlayers();
            }
        }

        return false;
    }
}
