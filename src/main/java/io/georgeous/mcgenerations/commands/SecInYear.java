package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.role.RoleManager;
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
        PlayerRole playerRole = RoleManager.get(player);

        if (args.length != 1) {
            sender.sendMessage("Usage: /secinyear 60");
        }

        playerRole.am.setSecInYear(Integer.parseInt(args[0]));


        return false;
    }
}
