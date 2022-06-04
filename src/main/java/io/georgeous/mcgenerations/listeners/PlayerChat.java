package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
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

import java.util.Random;

public class PlayerChat implements Listener {

    private final double CHAT_RANGE = 100;
    private final int ALONE_MSG_TIMER = 2; // secs
    private final boolean FILTER_PROFANITY = true;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        PlayerRole playerRole = RoleManager.get().get(player);

        if (playerRole == null) {
            return;
        }
        PhaseManager pm = playerRole.getPhaseManager();
        String firstName = playerRole.getName();
        String lastName = playerRole.getFamily().getColoredName() + "§f: ";
        lastName = playerRole.getFamily().getColor() +  playerRole.getFamily().getName().substring(0, 1) + ".";

        TextComponent prefix = new TextComponent(firstName + " " + lastName + "§f: ");
        TextComponent msg = new TextComponent(prepareMsg(event.getMessage(), pm.getCurrentPhase().getSpellAccuracy()));

        rangedBroadcast(player, prefix, msg, CHAT_RANGE);
    }


    public void rangedBroadcast(Player sender, TextComponent prefix, TextComponent msg, double range) {
        boolean messageReceived = false;
        for (Player receivingPlayer : Bukkit.getOnlinePlayers()) {
            if(receivingPlayer.getLocation().getWorld() == sender.getLocation().getWorld()){
                double distanceBetweenPlayers = receivingPlayer.getLocation().distance(sender.getLocation());
                messageReceived = handleReceivingMessage(receivingPlayer, sender, distanceBetweenPlayers, prefix, msg, range) || messageReceived;
            }

        }
        if(!messageReceived){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Notification.neutralMsg(sender, "Nobody heared you. Use §d[/howto chat]§f to learn more.");
                }
            }.runTaskLater(MCG.getInstance(), 20L * ALONE_MSG_TIMER);
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

    private void opEditPrefix(TextComponent prefix, Player sendingPlayer){
        prefix.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + sendingPlayer.getName()));
        prefix.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(sendingPlayer.getName()).color(ChatColor.GRAY).italic(true).create())
        );
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

        if(FILTER_PROFANITY){
            newMsg = BadWordFilter.getCensoredText(newMsg);
        }


        return newMsg;
    }
}


// if role
    // change name
    // Send to near Roles
    // Send to OP
    // Send to Player without role

// if no role
    // send to players without role
    // send to op