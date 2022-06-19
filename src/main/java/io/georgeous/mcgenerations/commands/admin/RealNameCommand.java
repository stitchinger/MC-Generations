package io.georgeous.mcgenerations.commands.admin;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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


public class RealNameCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only for players");
            return true;
        }

        if (!(player.isOp())) {
            sender.sendMessage("Only for OPs");
            return true;
        }

        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /realname <Name>");
            return true;
        }

        Player foundPlayer = NickAPI.getPlayerOfNickedName(args[0]);

        if(foundPlayer == null){
            return true;
        }

        TextComponent msg = new TextComponent(args[0] + "s real name is " );

        TextComponent clickableName = new TextComponent(foundPlayer.getName());
        clickableName.setColor(ChatColor.BLUE);
        clickableName.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, foundPlayer.getName()));
        clickableName.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to copy").color(ChatColor.GRAY).italic(true).create())
        );

        Notification.successMsg(player,  msg, clickableName );

        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();

        if (!(sender instanceof Player playerSender)) {
            return l;
        }

        if (cmd.getName().equalsIgnoreCase("realname")) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player != playerSender) {
                    PlayerRole role = RoleManager.get().get(player);
                    if (role != null) {
                        l.add(role.getName());
                    }
                }
            });
            return l;
        }

        return l;
    }

}