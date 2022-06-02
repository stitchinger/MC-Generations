package io.georgeous.mcgenerations.commands.admin;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.haoshoku.nick.api.NickAPI;
import xyz.haoshoku.nick.api.NickScoreboard;

public class NickCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
/*
        if (!(sender instanceof Player player))
            return true;

        if (!player.isOp()) {
            Notification.onlyForOp(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "/nick reset");
            player.sendMessage(ChatColor.YELLOW + "/nick <Name>");
            return true;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            NickAPI.resetNick(player);
            NickAPI.resetSkin(player);
            NickAPI.resetUniqueId(player);
            NickAPI.resetGameProfileName(player);
            NickAPI.refreshPlayer(player);
            player.sendMessage(ChatColor.DARK_RED + "Successfully reset nick");
        } else {
            String name = args[0].substring(0, 1).toUpperCase() + args[0].substring(1);
            nickPlayer(player, name);
            NickAPI.setSkin(player, name);
        }

 */
        return true;


    }
/*
    public void nickPlayer(Player player, String name) {
        NickAPI.nick(player, name);
        NickAPI.refreshPlayer(player);

        PlayerRole playerRole = RoleManager.getInstance().get(player);
        if (playerRole != null) {
            NickScoreboard.write(name, "admin", "", " " + playerRole.getFamily().getColoredName(), true, ChatColor.WHITE);
            NickScoreboard.updateScoreboard(name);
        }

        player.sendMessage(ChatColor.DARK_GREEN + "Successfully set the nickname to " + ChatColor.YELLOW + name);
    }

 */
}