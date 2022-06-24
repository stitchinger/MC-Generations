package io.georgeous.mcgenerations.commands.admin;

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
import xyz.haoshoku.nick.api.NickAPI;

import java.util.ArrayList;
import java.util.List;


public class InvseeCommand implements CommandExecutor, TabCompleter {

    private final List<String> reportReasons = new ArrayList<>();

    public InvseeCommand() {
        reportReasons.add("Cheating");
        reportReasons.add("Griefing");
        reportReasons.add("Exploiting");
        reportReasons.add("Profanity");
        reportReasons.add("Bullying");
        reportReasons.add("Other");
    }


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


        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /report <Name> <Reason>");
            return true;
        }


        Player target = NickAPI.getPlayerOfNickedName(args[0]);

        if(target == null)
            return true;

        player.openInventory(target.getInventory());

        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();

        if (!(sender instanceof Player playerSender)) {
            return l;
        }

        if (cmd.getName().equalsIgnoreCase("invsee")) {

            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player != playerSender) {
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