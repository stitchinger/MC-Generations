package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.utils.BadWordFilter;
import io.georgeous.mcgenerations.utils.Notification;
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
import org.bukkit.scheduler.BukkitRunnable;

import javax.management.relation.Role;
import java.util.Random;

public class PlayerChatListener implements Listener {

    /*
    private final double CHAT_RANGE = 100;
    private final int ALONE_MSG_TIMER = 2; // secs
    private final boolean FILTER_PROFANITY = true;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player sendingPlayer = event.getPlayer();
        PlayerWrapper sendingPlayerWrapper = PlayerManager.get().getWrapper(sendingPlayer);
        PlayerRole sendingPlayerRole = RoleManager.get().get(sendingPlayer);

        if(sendingPlayerRole != null){
            // change name in Message
            sendWithRole(sendingPlayerRole);
            return;
        }
    }

    private void sendWithRole(PlayerRole sendingPlayerRole){
        double range = 100;
        Player sendingPlayer = sendingPlayerRole.getPlayer();

        for (Player receivingPlayer : Bukkit.getOnlinePlayers()) {
            PlayerWrapper receivingPlayerWrapper = PlayerManager.get().getWrapper(receivingPlayer);
            PlayerRole receivingPlayerRole = RoleManager.get().get(receivingPlayer);
            double distanceBetweenPlayers = sendingPlayerRole.getPlayer().getLocation().distance(receivingPlayer.getLocation());


        }
    }

    public boolean handleReceivingMessage(Player receivingPlayer, Player sendingPlayer,  double distanceBetweenPlayers, TextComponent prefix, TextComponent msg, double range){
        boolean messageReceived = false;
        TextComponent customPrefix = prefix.duplicate();


        // Adjust chat color to distance
        if(distanceBetweenPlayers < range * (1d/3d)){
            customPrefix.setColor(ChatColor.WHITE);
        } else if(distanceBetweenPlayers < range * (2d/3d)){
            customPrefix.setColor(ChatColor.GRAY);
        } else if(distanceBetweenPlayers < range){
            customPrefix.setColor(ChatColor.DARK_GRAY);
        } else{
            customPrefix.setColor(ChatColor.DARK_GRAY);
            customPrefix.setStrikethrough(true);
        }

        if (distanceBetweenPlayers <= range) {
            if(sendingPlayer != receivingPlayer){
                messageReceived = true;
            }
            if(receivingPlayer.isOp()){
                opEditPrefix(customPrefix, sendingPlayer);
            }
            receivingPlayer.spigot().sendMessage( customPrefix, msg);

        } else if(receivingPlayer.isOp()){
            opEditPrefix(customPrefix, sendingPlayer);
            receivingPlayer.spigot().sendMessage(customPrefix, msg);
        }

        return  messageReceived;
    }

*/
}


