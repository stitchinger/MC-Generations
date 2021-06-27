package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Me implements CommandExecutor {

    private final Main main;

    public Me(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This command is for players only!");
            return true;
        }

        if(args.length == 0){
            Player p = (Player) sender;
            PlayerWrapper cp = PlayerManager.get(p);
            PlayerRole pr = cp.playerRole;


            sender.sendMessage("You are " + cp.playerRole.firstName + " " + cp.playerRole.family.getName() + ".");
            sender.sendMessage("You are " + cp.playerRole.am.ageInYears +  " year(s) old.");
        }
        return true;
    }
}
