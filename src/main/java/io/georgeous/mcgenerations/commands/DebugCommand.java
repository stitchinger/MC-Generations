package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (!player.isOp()) {
            Notification.onlyForOp(player);
            return true;
        }

        if (args.length < 1) {
            return true;
        }

        switch (args[0]) {
            case "council":
                player.teleport(MCG.council.councilLocation);
                break;
            default:

        }
        return false;
    }
}