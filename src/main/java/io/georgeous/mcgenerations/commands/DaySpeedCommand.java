package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DaySpeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if (!player.isOp()) {
            Notification.onlyForOp(player);
            return true;
        }

        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /dayspeed 100");
        }

        //playerRole.am.setSecInYear(Integer.parseInt(args[0]));
        MCG.daySpeed = Long.parseLong(args[0]);


        return false;
    }
}