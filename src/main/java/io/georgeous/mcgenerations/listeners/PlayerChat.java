package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.haoshoku.nick.api.NickAPI;

public class PlayerChat implements Listener {

    private final double CHAT_RANGE = 50;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        PlayerRole playerRole = RoleManager.get(player);
        String realName = NickAPI.getOriginalName(player);

        if (playerRole == null) {
            return;
        }

        PhaseManager pm = playerRole.pm;

        TextComponent prefix = new TextComponent(playerRole.getName() + " " + playerRole.family.getColoredName() + "§f: ");

        TextComponent msg = new TextComponent(event.getMessage());


        if(player.isOp()){
            prefix.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp @s " + realName));
            prefix.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(realName).color(ChatColor.GRAY).italic(true).create())
            );
        }

        rangedBroadcastNew(player, prefix, msg, CHAT_RANGE);
    }

    public String prepareMsg(String msg, String prefix, int maxLength) {
        msg = msg.trim();
        msg = msg.substring(0, Math.min(msg.length(), maxLength));
        msg = prefix + msg;

        return msg;
    }

    public void rangedBroadcastNew(Player sender, TextComponent prefix, TextComponent msg, double range) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            double distanceBetweenPlayers = other.getLocation().distance(sender.getLocation());
            if (distanceBetweenPlayers <= range || other.isOp()) {
                other.spigot().sendMessage(prefix, msg);
            }
        }
    }
}
