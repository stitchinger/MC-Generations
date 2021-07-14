package io.georgeous.mcgenerations.role.commands;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.role.PlayerRole;
import io.georgeous.mcgenerations.role.RoleManager;
import io.georgeous.piggyback.Piggyback;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class YouAre implements CommandExecutor {

    private final MCG main;

    public YouAre(MCG main) {
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
            Player player = (Player) sender;
            PlayerRole playerRole = RoleManager.get(player);
            nameChild(player, playerRole, args[1]);
        }
        return true;
    }

    public void nameChild(Player nameGiver, PlayerRole playerRole, String rawName){
        if(Piggyback.carryPairs.get(nameGiver) == null){
            nameGiver.sendMessage("You need to hold your baby for naming it.");
            return;
        }

        Entity target = Piggyback.carryPairs.get(nameGiver).getTarget();
        String first = rawName.substring(0, 1).toUpperCase() + rawName.substring(1);

        if(target instanceof Player){
            PlayerRole targetsPlayerRole = RoleManager.get((Player)target);
            
            if(!targetsPlayerRole.isRenamed()){
                Piggyback.stopCarry(nameGiver);
                targetsPlayerRole.setName(first);
            }else{
                nameGiver.sendMessage("You can name your child only once");
            }
            //NameManager.name((Player) target,first, cp.family.getName());
        }else{
            target.setCustomName(first + " " + playerRole.family.getColoredName());
        }
    }
}
