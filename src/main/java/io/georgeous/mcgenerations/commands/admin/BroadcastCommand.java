package io.georgeous.mcgenerations.commands.admin;


import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.isOp()) {
                Notification.onlyForOp(player);
                return true;
            }
        }

        if (args.length == 0) {
            return true;
        }

        String msg = "§6SERVER MSG: ";

        for (String arg : args) {
            msg = msg + " " + String.valueOf(arg);
        }

        Bukkit.broadcastMessage(msg);

        return false;
    }


}