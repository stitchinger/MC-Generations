package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.role.RoleManager;
import io.georgeous.piggyback.events.PlayerStartCarryEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.management.relation.Role;


public class PlayerCarry implements Listener {

    @EventHandler
    public void onCarryStart(PlayerStartCarryEvent event){
        Player player = event.getPlayer();
        Entity target = event.getTarget();

        if(target instanceof Player){
            PlayerRole targetRole = RoleManager.get((Player)target);
            PlayerRole playerRole = RoleManager.get(player);

            if(playerRole == null || targetRole == null){
                event.setCancelled(true);
                return;
            }

            boolean canCarry = playerRole.pm.getCurrentPhase().canCarry();
            boolean canBeCarried = targetRole.pm.getCurrentPhase().canBeCarried();

            if(!canBeCarried || !canCarry){
                event.setCancelled(true);
            }
        }
    }
}
