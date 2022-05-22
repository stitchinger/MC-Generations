package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerConnection implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage("Welcome to One Hour One Life");
        player.sendMessage("Join the Discord - " + ChatColor.BLUE + ChatColor.UNDERLINE + "https://discord.gg/U262bxT4jh");

        event.setJoinMessage("");
    }
}