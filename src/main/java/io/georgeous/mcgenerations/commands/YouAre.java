package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import io.georgeous.piggyback.Piggyback;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class YouAre implements CommandExecutor {

    private final Main main;

    public YouAre(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This command is for players only!");
            return true;
        }

        if(args.length != 2){
            sender.sendMessage("Usage: /You are Lisa");
        }

        if(args.length == 2){
            Player p = (Player) sender;
            PlayerWrapper cp = PlayerManager.get(p);
            nameChild(p,cp, args[1]);
        }
        return true;
    }

    public void nameChild(Player p, PlayerWrapper cp, String rawName){
        if(Piggyback.carryPairs.get(p) != null){
            Entity target = Piggyback.carryPairs.get(p);
            String first = rawName.substring(0, 1).toUpperCase() + rawName.substring(1);
            if(target instanceof Player){
                // if target wasnt named before
                PlayerWrapper cpTarget = PlayerManager.get((Player)target);
                if(!cpTarget.getRole().isNamed()){
                    cpTarget.getRole().firstName = first;
                    cpTarget.getRole().setNamed(true);
                }
                //NameManager.name((Player) target,first, cp.family.getName());
            }else{
                target.setCustomName(first + " " + cp.getRole().family.getName());
            }
        }else{
            p.sendMessage("You need to hold your baby for naming it.");
        }
    }
}
