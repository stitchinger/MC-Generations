package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.files.Reporter;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ReportCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only for players");
            return true;
        }

        if (args.length > 2 || args.length == 0) {
            Notification.errorMsg(player, "Usage: /report <Name> <Reason>");
            return true;
        }

        //String arg = args[0].toLowerCase();

        switch (args[0]) {

            case "list":
                if (!player.isOp()) {
                    Notification.errorMsg(player, "Only for OPs");
                    return true;
                }
                Reporter.printReports(player);
                break;

            case "reload":
                if (!player.isOp()) {
                    Notification.errorMsg(player, "Only for OPs");
                    return true;
                }
                Reporter.reload();
                break;

            default:
                if (args.length != 2) {
                    Notification.errorMsg(player, "Usage: /report <Name> <Reason>");
                } else {
                    Reporter.reportPlayer(player, args[0], args[1]);
                }
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();

        if (!(sender instanceof Player playerSender)) {
            return l;
        }

        if (cmd.getName().equalsIgnoreCase("report")) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if(player != playerSender ){
                    PlayerRole role = RoleManager.get().get(player);
                    if (role != null) {
                        l.add(role.getName());
                    } else {
                        l.add(player.getName());
                    }
                }
            });
            return l;
        }
        return l;
    }
}