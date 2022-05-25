package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
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

        playerManager.initPlayer(event.getPlayer());
        roleManager.initPlayer(event.getPlayer());

        player.sendMessage("Welcome to One Hour One Life");

        BaseComponent[] component = new ComponentBuilder("Join the")
                .append("Discord!").color(ChatColor.BLUE).underlined(true)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/U262bxT4jh"))
                .create();
        player.spigot().sendMessage(component);

        event.setJoinMessage("");
    }


}
