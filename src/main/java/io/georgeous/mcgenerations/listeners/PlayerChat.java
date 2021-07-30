package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    private final double CHAT_RANGE = 50;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        event.setCancelled(true);
        Player player = event.getPlayer();
        PlayerRole playerRole = RoleManager.get(player);

        if(playerRole == null){
            return;
        }

        PhaseManager pm = playerRole.pm;

        String prefix = playerRole.getName() + " " + playerRole.family.getColoredName() + "§f: ";
        String msg = prepareMsg(event.getMessage(), prefix, pm.getCurrentPhase().getMaxCharsInChat());

        rangedBroadcast(player, msg, CHAT_RANGE);
    }

    public String prepareMsg(String msg, String prefix, int maxLength){
        msg = msg.trim();
        msg = msg.substring(0,Math.min(msg.length(),maxLength));
        msg = prefix + msg;

        return msg;
    }

    public void rangedBroadcast(Player sender, String msg, double range){
        for (Player other : Bukkit.getOnlinePlayers()) {
            double distanceBetweenPlayers = other.getLocation().distance(sender.getLocation());
            if (distanceBetweenPlayers <= range) {
                other.sendMessage(msg);
            }
        }
    }

}
