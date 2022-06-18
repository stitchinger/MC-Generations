package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.files.McgConfig;
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

    private final double CHAT_RANGE = 50;
/*
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        //String msg = event.setFormat();
        PlayerRole role = RoleManager.get().get(event.getPlayer());

        if(role != null){
           // sendWithRole();

        } else{
            //sendWithoutRole();
        }

        T//extComponent chatName = new TextComponent(firstName + " " + lastName + "§f: ");
        TextComponent chatMsg = new TextComponent(prepareMsg(event.getMessage(), pm.getCurrentPhase().getSpellAccuracy()));
        //event.setCancelled(true);
    }

    public void sendWithRole(Player sender, TextComponent chatName, TextComponent chatMsg){
        for (Player receivingPlayer : Bukkit.getOnlinePlayers()) {
            PlayerRole receiverRole = RoleManager.get().get(receivingPlayer);

            if(receiverRole != null){
                if(receivingPlayer.getLocation().getWorld() != sender.getLocation().getWorld()){
                    continue;
                }
                double distanceBetweenPlayers = receivingPlayer.getLocation().distance(sender.getLocation());
                TextComponent name = formatChatName(chatName, distanceBetweenPlayers);
            }


        }
    }

    private boolean roleReceiveMsg(){

    }

    public void rangedBroadcast(Player sender, TextComponent prefix, TextComponent msg, double range) {
        boolean messageReceived = false;
        for (Player receivingPlayer : Bukkit.getOnlinePlayers()) {
            if(receivingPlayer.getLocation().getWorld() != sender.getLocation().getWorld()){
                continue;
            }
            double distanceBetweenPlayers = receivingPlayer.getLocation().distance(sender.getLocation());
            messageReceived = handleReceivingMessage(receivingPlayer, sender, distanceBetweenPlayers, prefix, msg, range) || messageReceived;

        }
        if(!messageReceived){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Notification.neutralMsg(sender, "Nobody heared you. Use §d[/howto chat]§f to learn more.");
                }
            }.runTaskLater(MCG.getInstance(), 20L * McgConfig.getChatAloneMsgTime());
        }
    }

    public String prepareMsg(String msg, float spellAccuracy) {
        msg = msg.trim();
        msg = msg.substring(0, Math.min(msg.length(), 999));
        String newMsg = "";
        for(char c : msg.toCharArray()) {
            char replacement;

            if(Math.random() <= spellAccuracy){
                replacement = c;
            } else{
                Random random = new Random();
                char randomizedCharacter = (char) (random.nextInt(26) + 'a');
                replacement = randomizedCharacter;
            }

            if(Math.random() <= spellAccuracy){
                newMsg = newMsg + replacement;
            }
        }

        if(McgConfig.getChatFilterProfanity()){
            newMsg = BadWordFilter.getCensoredText(newMsg);
        }


        return newMsg;
    }

    public TextComponent formatChatName(TextComponent prefix, double distance){
        TextComponent customPrefix = prefix.duplicate();

        // Adjust chat color to distance
        if(distance < CHAT_RANGE * (1d/3d)){
            customPrefix.setColor(ChatColor.WHITE);
        } else if(distance < CHAT_RANGE * (2d/3d)){
            customPrefix.setColor(ChatColor.GRAY);
        } else if(distance < CHAT_RANGE){
            customPrefix.setColor(ChatColor.DARK_GRAY);
        } else{
            customPrefix.setColor(ChatColor.DARK_GRAY);
            customPrefix.setStrikethrough(true);
        }

        return customPrefix;
    }


 */
}


