package io.georgeous.mcgenerations.commands.player;
import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AdCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        String ad = "/ad 1hour1life NEW UNIQUE GAMEMODE! Join as a baby of a random player. Build your Legacy!";

        TextComponent msg = new TextComponent("Click here to copy the Ad message to your clipboard. Thanks for supporting the server :)");
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "/ad 1hour1life NEW UNIQUE GAMEMODE! Join as a baby of a random player. Build your Legacy! "));
        msg.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to copy").color(ChatColor.GRAY).italic(true).create())
        );

        player.spigot().sendMessage(msg);

        return false;
    }


}