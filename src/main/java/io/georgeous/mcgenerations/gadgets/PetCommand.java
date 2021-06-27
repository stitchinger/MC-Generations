package io.georgeous.mcgenerations.gadgets;

import io.georgeous.mcgenerations.Main;

import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.List;

public class PetCommand implements CommandExecutor {

    private final Main main;

    public PetCommand() {
        this.main = Main.getPlugin();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }

        Player p = (Player) sender;
        Player playerB = Util.findClosestPlayer(p, 5d);

        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("release")){
                PetManager.releasePets(p);
            } else if(args[0].equalsIgnoreCase("passon")){
                PetManager.passPets(p,playerB);
            } else if(args[0].equalsIgnoreCase("count")){
                p.sendMessage("Pets: " + PetManager.ownerPets.get(p).size());
            }
        }

        return true;
    }

}
