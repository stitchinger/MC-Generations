package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import xyz.haoshoku.nick.api.NickAPI;
import xyz.haoshoku.nick.api.NickScoreboard;

import java.util.Collection;

public class NickCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

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

        if ("reset".equalsIgnoreCase(args[0])) {
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
        return true;
    }

    public void nickPlayer(Player player, String name) {
        NickAPI.nick(player, name);
        NickAPI.refreshPlayer(player);

        if (RoleManager.get(player) != null) {
            PlayerRole playerRole = RoleManager.get(player);
            NickScoreboard.write(name, "admin", "", " " + playerRole.getFamily().getColoredName(), false, ChatColor.WHITE);
            NickScoreboard.updateScoreboard(name);
        }
        player.sendMessage(ChatColor.DARK_GREEN + "Successfully set the nickname to " + ChatColor.YELLOW + name);
    }

    // TODO: remove private method that is never used

//    private void updateNickNamesToScoreboard(Player player) {
//        if (player == null)
//            throw new NullPointerException("Player cannot be null");
//
//        Scoreboard scoreboard;
//        // Change it, if you are using main scoreboard
//        if (player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard()) {
//            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
//            player.setScoreboard(scoreboard);
//        } else
//            scoreboard = player.getScoreboard();
//
//        if (scoreboard.getTeam("nickedTeam") != null) scoreboard.getTeam("nickedTeam").unregister();
//
//        Team team = scoreboard.registerNewTeam("nickedTeam");
//        team.setPrefix("MyPrefix ");
//        team.setSuffix("MySuffix ");
//
//        Collection<String> values = NickAPI.getNickedPlayers().values();
//        for (String name : values)
//            scoreboard.getTeam("nickedTeam").addEntry(name);
//    }
}