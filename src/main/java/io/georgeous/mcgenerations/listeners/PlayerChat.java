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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        PlayerWrapper playerWrapper = PlayerManager.get(player);
        PhaseManager pm = playerWrapper.playerRole.pm;
        pm.getCurrentPhase().maxWords = 0;


        String msg = event.getMessage();
        String[] words = msg.trim().substring(0,Math.min(msg.length(),10)).split(" ");

        String newMsg = "";
        for(String s : words){
            newMsg = newMsg + " " + s.substring(0,Math.min(s.length(),3));
        }

        newMsg = newMsg.trim();


        double maxDist = 50;
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.getLocation().distance(player.getLocation()) <= maxDist) {
                player.sendMessage(playerWrapper.playerRole.firstName + " " + playerWrapper.playerRole.family.getName() + "§f: " + newMsg);
            }
        }

        event.setCancelled(true);
    }

}
