package io.georgeous.mcgenerations.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Notification {

    public static void neutralMsg(Player player, String msg) {
        String prefix = ChatColor.YELLOW + "[i] " + ChatColor.RESET + ChatColor.GRAY;
        player.sendMessage(prefix + msg);
    }

    public static void errorMsg(Player player, String msg) {
        String prefix = ChatColor.RED + "[!] " + ChatColor.RESET + ChatColor.GRAY;
        player.sendMessage(prefix + msg);
    }

    public static void successMsg(Player player, String msg) {
        String prefix = ChatColor.GREEN + "[!] " + ChatColor.RESET + ChatColor.GRAY;
        player.sendMessage(prefix + msg);
    }

    public static void onlyForOp(Player player) {
        errorMsg(player, "This command is only for OPs");
    }

    public static void onlyForPlayers(CommandSender sender) {
        String prefix = ChatColor.RED + "[!]" + ChatColor.RESET;
        sender.sendMessage(prefix + "This command is only for players.");
    }

    public static void opBroadcast(String msg){
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(player.isOp()){
                errorMsg(player, msg);
            }
        });
    }

    public static void opBroadcast(TextComponent... args){

        Bukkit.getOnlinePlayers().forEach(player -> {
            if(player.isOp()){
                //Object[] test = Arrays.stream(args).toArray();
                player.spigot().sendMessage(ChatMessageType.SYSTEM,  args);
            }
        });
    }

}