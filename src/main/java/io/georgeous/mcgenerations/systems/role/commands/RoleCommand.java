package io.georgeous.mcgenerations.systems.role.commands;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RoleCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        if (!player.isOp()) {
            Notification.onlyForOp(player);
            return true;
        }

        PlayerRole playerRole = RoleManager.get(player);
        if (playerRole == null) {
            Notification.errorMsg(player, "No role found");
            return true;
        }

        switch (args[0]) {
            case "age":
                if (args.length < 2 || args[1].length() == 0) {
                    Notification.errorMsg(player, "e.g.: /role age 13");
                    return true;
                }
                try {
                    int age = Integer.parseInt(args[1]);
                    playerRole.am.setAge(age);
                    Notification.successMsg(player, "You changed your age to " + age);
                } catch (NumberFormatException e) {
                    Notification.errorMsg(player, "Invalid age");
                }

                break;
            case "info":
                Notification.neutralMsg(player, "Name: " + playerRole.getName());
                Notification.neutralMsg(player, "Age: " + playerRole.am.getAge());
                break;
            case "count":
                Notification.neutralMsg(player, "Roles in RoleManager: " + RoleManager.getRoleCount());
                break;
            default:

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> l = new ArrayList<String>();
        if (cmd.getName().equalsIgnoreCase("role") && args.length >= 0) {
            if (sender instanceof Player) {
                l.add("age");
                l.add("info");
                l.add("count");
                return l;
            }
        }
        return l;
    }
}