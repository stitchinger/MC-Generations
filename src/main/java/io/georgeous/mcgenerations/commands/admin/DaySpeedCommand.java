package io.georgeous.mcgenerations.commands.admin;

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
        if (!(sender instanceof Player player)) {
            return true;
        }
        if (!player.isOp()) {
            Notification.onlyForOp(player);
            return true;
        }

        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /dayspeed 100");
        }

        MCG.daySpeed = Long.parseLong(args[0]);

        return false;
    }
}