package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
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
import xyz.haoshoku.nick.api.NickAPI;
import java.util.Random;

public class PlayerChat implements Listener {

    private final double CHAT_RANGE = 9999999;
    private final int ALONE_MSG_TIMER = 2; // secs

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        PlayerRole playerRole = RoleManager.getInstance().get(player);

        if (playerRole == null) {
            return;
        }

        PhaseManager pm = playerRole.getPhaseManager();

        TextComponent prefix = new TextComponent(playerRole.getName() + " " + playerRole.family.getColoredName() + "§f: ");
        TextComponent msg = new TextComponent(prepareMsg(event.getMessage(), pm.getCurrentPhase().getMaxCharsInChat()));

        rangedBroadcast(player, prefix, msg, CHAT_RANGE);
    }

    public String prepareMsg(String msg, int maxLength) {
        double correctSpellingChance = 1;
        msg = msg.trim();
        msg = msg.substring(0, Math.min(msg.length(), maxLength));
        String newMsg = "";
        for(char c : msg.toCharArray()) {
            char replacement;

            if(Math.random() <= correctSpellingChance){
                replacement = c;
            } else{
                Random random = new Random();
                char randomizedCharacter = (char) (random.nextInt(26) + 'a');
                replacement = randomizedCharacter;
            }

            if(Math.random() <= correctSpellingChance){
                newMsg = newMsg + replacement;
            }
        }
        return newMsg;
    }


    public void rangedBroadcast(Player sender, TextComponent prefix, TextComponent msg, double range) {
        boolean messageReceived = false;
        for (Player receivingPlayer : Bukkit.getOnlinePlayers()) {
            double distanceBetweenPlayers = receivingPlayer.getLocation().distance(sender.getLocation());
            TextComponent customPrefix = prefix.duplicate();
            if (distanceBetweenPlayers <= range) {
                if(sender != receivingPlayer){
                    messageReceived = true;
                }
                if(receivingPlayer.isOp()){
                    prefix.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "" + sender.getName()));
                    prefix.setHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder(sender.getName()).color(ChatColor.GRAY).italic(true).create())
                    );
                }
                receivingPlayer.spigot().sendMessage(prefix, msg);
            } else if(receivingPlayer.isOp()){
                prefix.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "" + sender.getName()));
                prefix.setHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(sender.getName()).color(ChatColor.GRAY).italic(true).create())
                );
                receivingPlayer.spigot().sendMessage(prefix, msg);
            }

        }
        if(!messageReceived){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Notification.neutralMsg(sender, "Nobody heared you. You must be too far away!");
                }
            }.runTaskLater(MCG.getInstance(), 20L * ALONE_MSG_TIMER);
        }
    }
}