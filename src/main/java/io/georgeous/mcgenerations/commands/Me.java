package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.role.PlayerRole;
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

        Player player = (Player) sender;
        PlayerWrapper playerWrapper = PlayerManager.get(player);
        PlayerRole playerRole = playerWrapper.getRole();

        if(playerRole == null){
            sender.sendMessage("No PlayerRole attached");
            return true;
        }

        sender.sendMessage("You are " + playerRole.getName() + " " + playerRole.family.getName() + ".");
        sender.sendMessage("You are " + playerRole.am.ageInYears +  " year(s) old.");

        return true;
    }
}
