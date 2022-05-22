package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SurrogateListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        SurrogateManager.getInstance().destroyPlayer(event.getEntity());
    }
}