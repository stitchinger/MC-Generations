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
import java.util.Random;


public class AdCommand implements CommandExecutor {

    String[] ads = {
            "/ad 1hour1life NEW UNIQUE GAMEMODE! Join as a baby of a random player. Build your Legacy!",
            "/ad 1hour1life Hardcore Family Survival Civilization Simulator"

    };


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        Random r = new Random();
        int randomNumber=r.nextInt(ads.length);
        String ad = ads[randomNumber];

        TextComponent msg = new TextComponent("Click here to copy the Ad message to your clipboard. Thanks for supporting the server :)");
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, ad));
        msg.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to copy").color(ChatColor.GRAY).italic(true).create())
        );

        Notification.successMsg(player, msg);
        return false;
    }


}