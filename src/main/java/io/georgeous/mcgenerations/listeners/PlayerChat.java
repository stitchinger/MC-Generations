package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.lifephase.PhaseManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    private final double CHAT_RANGE = 50;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        PlayerWrapper playerWrapper = PlayerManager.get(player);
        PhaseManager pm = playerWrapper.getRole().pm;
        //pm.getCurrentPhase().maxCharsInChat;

        String msg = event.getMessage();
        msg = msg.trim();
        msg = msg.substring(0,Math.min(msg.length(),pm.getCurrentPhase().maxCharsInChat));
        //String[] words = msg.trim().substring(0,Math.min(msg.length(),10)).split(" ");

        /*
        String newMsg = "";
        for(String s : words){
            newMsg = newMsg + " " + s.substring(0,Math.min(s.length(),3));
        }

        newMsg = newMsg.trim();
         */

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.getLocation().distance(player.getLocation()) <= CHAT_RANGE) {
                player.sendMessage(playerWrapper.getRole().getName() + " " + playerWrapper.getRole().family.getName() + "§f: " + msg);
            }
        }

        event.setCancelled(true);
    }

}
