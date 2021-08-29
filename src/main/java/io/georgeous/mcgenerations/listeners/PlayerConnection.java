package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnection implements Listener {
    private final MCG main;

    public PlayerConnection() {
        this.main = MCG.getInstance();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage("Welcome to One Hour One Life");
        player.sendMessage("Join the Discord - " + ChatColor.BLUE + ChatColor.UNDERLINE + "https://discord.gg/U262bxT4jh");
    }
}