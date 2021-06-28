package io.georgeous.mcgenerations.commands;

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

        Player p = (Player) sender;

        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("surrogate")){
                if(SurroManager.map.get(p) != null){
                    SurroManager.destroySurrogate(p);
                }else{
                    SurroManager.create(p);
                }

            } else if(args[0].equalsIgnoreCase("pets")){


            } else if(args[0].equalsIgnoreCase("banner")){ // Banner
                ItemStack hand = p.getInventory().getItemInMainHand();
                //p.sendMessage(hand.toString());
                if(hand.getItemMeta() instanceof BannerMeta){
                    p.sendMessage(((BannerMeta) hand.getItemMeta()).getPatterns().toString());
                    for(Pattern pat : ((BannerMeta) hand.getItemMeta()).getPatterns()){
                        pat.getPattern().equals(PatternType.CREEPER);
                    }
                }
            } else if(args[0].equalsIgnoreCase("saveplayer")){ // Banner

                PlayerManager.savePlayer();

            }else if(args[0].equalsIgnoreCase("council")){ // Council Teleport
                p.teleport(new Location(p.getWorld(),0,250,0));

            }
            else if(args[0].equalsIgnoreCase("exp")){ // Council Teleport


            }
            else if(args[0].equalsIgnoreCase("save")){ //
                PlayerManager.savePlayer();

            }
            else if(args[0].equalsIgnoreCase("load")){ //
                PlayerManager.restorePlayer();
            }
        }

        return false;
    }
}
