package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.files.McgConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;


public class CreatureSpawnListener implements Listener {

    @EventHandler
    public void disableMobsInCouncil(CreatureSpawnEvent event) {
        final float DISABLE_RADIUS = 400;
        Location centerPoint = McgConfig.getCouncilLocation();
        Entity entity = event.getEntity();

        // Not Overworld?
        if(event.getLocation().getWorld() != MCG.overworld){
            return;
        }

        double distance = centerPoint.distance(event.getLocation());

        if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL){
            return;
        }

        if(distance > DISABLE_RADIUS){
            return;
        }

        if(entity instanceof Creeper ||
                entity instanceof Zombie ||
                entity instanceof Skeleton ||
                entity instanceof Spider ||
                entity instanceof Drowned ||
                entity instanceof ZombieVillager ||
                entity instanceof Enderman ||
                entity instanceof ZombieHorse ||
                entity instanceof Witch
        ){
            event.setCancelled(true);
        }
    }

}
