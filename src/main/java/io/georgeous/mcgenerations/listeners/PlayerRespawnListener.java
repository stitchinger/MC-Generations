package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    private final RoleManager roleManager = RoleManager.getInstance();
    PlayerManager playerManager = PlayerManager.getInstance();

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.getHealth() == 0){
            SpawnManager.spawnPlayer(player);
        }
    }

}
