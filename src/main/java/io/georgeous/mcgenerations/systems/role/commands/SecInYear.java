package io.georgeous.mcgenerations.systems.role.commands;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SecInYear implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if (!player.isOp()) {
            Notification.onlyForOp(player);
            return true;
        }
        PlayerRole playerRole = RoleManager.get(player);

        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /secinyear 60");
            return true;
        }
        try {
            int secs = Integer.parseInt(args[0]);
            playerRole.am.setSecInYear(secs);
            Notification.successMsg(player, "You changed your sec in year to " + secs);

        } catch (NumberFormatException e) {
            Notification.errorMsg(player, "Argument must be a number");
            return true;
        }


        return false;
    }
}