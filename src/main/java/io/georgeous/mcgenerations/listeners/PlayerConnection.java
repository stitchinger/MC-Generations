package io.georgeous.mcgenerations.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnection implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage("Welcome to One Hour One Life");

        BaseComponent[] component = new ComponentBuilder("Join the")
                .append("Discord!").color(ChatColor.BLUE).underlined(true)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/U262bxT4jh"))
                .create();
        player.spigot().sendMessage(component);
    }
}