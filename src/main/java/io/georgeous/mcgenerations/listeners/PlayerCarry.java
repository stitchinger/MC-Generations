package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.piggyback.events.PlayerStartCarryEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCarry implements Listener {

    @EventHandler
    public void onCarryStart(PlayerStartCarryEvent event) {
        Player player = event.getPlayer();
        Entity target = event.getTarget();

        if (!(target instanceof Player) && !(target instanceof Cow) && !(target instanceof Chicken) && !(target instanceof Sheep) && !(target instanceof Pig)){
            event.setCancelled(true);
            return;
        }

        if(!(target instanceof Player)){
            return;
        }

        PlayerRole targetRole = RoleManager.get().get((Player) target);
        PlayerRole playerRole = RoleManager.get().get(player);

        if (playerRole == null || targetRole == null) {
            event.setCancelled(true);
            return;
        }

        boolean canCarry = playerRole.getPhaseManager().getCurrentPhase().canCarry();
        boolean canBeCarried = targetRole.getPhaseManager().getCurrentPhase().canBeCarried();

        if (!canBeCarried || !canCarry) {
            event.setCancelled(true);
            return;
        }

        // Effects for player as target
        startCarryEffects(target.getLocation());
    }

    private void startCarryEffects(Location location) {
        try {
            World world = location.getWorld();
            if (world == null)
                return;
            world.spawnParticle(Particle.HEART, location, 10, 0.5, 0.5, 0.5);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}