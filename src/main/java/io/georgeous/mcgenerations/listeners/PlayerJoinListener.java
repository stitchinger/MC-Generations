package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final RoleManager roleManager = RoleManager.getInstance();
    PlayerManager playerManager = PlayerManager.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);  // Just in case
        welcomeMessage(player);

        playerManager.initPlayer(player);
        roleManager.initPlayer(player);

        event.setJoinMessage("");
    }

    private void welcomeMessage(Player player){
        player.sendMessage("§6Welcome to §l§cONE §fHOUR §cONE §fLIFE");
        BaseComponent[] component = new ComponentBuilder("Please click here to join our ")
                .color(ChatColor.GOLD)
                .append("Discord!").color(ChatColor.BLUE).underlined(true)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/U262bxT4jh"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to visit!").color(ChatColor.GRAY).italic(true).create()))
                .create();
        player.spigot().sendMessage(component);
        player.sendMessage("");
    }
}
