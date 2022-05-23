package io.georgeous.mcgenerations.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Notification {

    public static void neutralMsg(Player player, String msg) {
        String prefix = ChatColor.YELLOW + "[i] " + ChatColor.RESET;
        player.sendMessage(prefix + msg);
    }

    public static void errorMsg(Player player, String msg) {
        String prefix = ChatColor.RED + "[!] " + ChatColor.RESET;
        player.sendMessage(prefix + msg);
    }

    public static void successMsg(Player player, String msg) {
        String prefix = ChatColor.GREEN + "[!] " + ChatColor.RESET;
        player.sendMessage(prefix + msg);
    }

    public static void onlyForOp(Player player) {
        errorMsg(player, "This command is only for OPs");
    }

    public static void onlyForPlayers(CommandSender sender) {
        String prefix = ChatColor.RED + "[!]" + ChatColor.RESET;
        sender.sendMessage(prefix + "This command is only for players.");
    }
}