package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
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

        Player p = (Player) sender;
        PlayerWrapper cp = PlayerManager.get(p);

        if (args.length != 1) {
            sender.sendMessage("Usage: /secinyear 60");
        }

        cp.getRole().am.setSecInYear(Integer.parseInt(args[0]));


        return false;
    }
}
