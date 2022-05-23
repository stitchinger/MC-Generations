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
import java.util.Random;

public class PlayerChat implements Listener {

    private final double CHAT_RANGE = 50;

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

        rangedBroadcastNew(player, prefix, msg, CHAT_RANGE);
    }

    public String prepareMsg(String msg, int maxLength) {
        double correctSpellingChance = .85;
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

    public void rangedBroadcastNew(Player sender, TextComponent prefix, TextComponent msg, double range) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            double distanceBetweenPlayers = other.getLocation().distance(sender.getLocation());
            if (distanceBetweenPlayers <= range || other.isOp()) {


                if(other.isOp()){
                    //prefix.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp @s " + sender.getName()));
                    prefix.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, " " + sender.getName()));
                    prefix.setHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder(sender.getName()).color(ChatColor.GRAY).italic(true).create())
                    );
                }

                other.spigot().sendMessage(prefix, msg);
            }
        }
    }
}