package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.piggyback.events.PlayerStartCarryEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCarry implements Listener {

    @EventHandler
    public void onCarryStart(PlayerStartCarryEvent event) {
        Player player = event.getPlayer();
        Entity target = event.getTarget();
        player.sendMessage("start carry");

        if (target instanceof Player) {
            player.sendMessage("is player");
            PlayerRole targetRole = RoleManager.get((Player) target);
            PlayerRole playerRole = RoleManager.get(player);

            if (playerRole == null || targetRole == null) {
                player.sendMessage("no role");
                event.setCancelled(true);
                return;
            }

            boolean canCarry = playerRole.pm.getCurrentPhase().canCarry();
            boolean canBeCarried = targetRole.pm.getCurrentPhase().canBeCarried();

            if (!canBeCarried || !canCarry) {
                player.sendMessage("no carry");
                event.setCancelled(true);
                return;
            }

            startCarryEffects(target.getLocation());
        } else if((target instanceof Chicken) == false){
            event.setCancelled(true);
        }
    }

    private void startCarryEffects(Location location) {
        try {
            location.getWorld().spawnParticle(Particle.HEART, location, 10, 0.5, 0.5, 0.5);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
}