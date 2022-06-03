package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.files.McgConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(event.getTo().distance(McgConfig.getCouncilLocation()) < 2){
            SpawnManager.spawnPlayer(player);
        }
    }
}
