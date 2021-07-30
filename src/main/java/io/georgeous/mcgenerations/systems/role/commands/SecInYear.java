package io.georgeous.mcgenerations.systems.role.commands;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SecInYear implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }

        Player player = (Player) sender;
        if(!player.isOp()){
            sender.sendMessage("This command is only for OPs");
            return true;
        }
        PlayerRole playerRole = RoleManager.get(player);

        if (args.length != 1) {
            sender.sendMessage("Usage: /secinyear 60");
        }

        playerRole.am.setSecInYear(Integer.parseInt(args[0]));


        return false;
    }
}
