package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import io.georgeous.piggyback.events.PlayerStartCarryEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class PlayerCarry implements Listener {

    @EventHandler
    public void onCarryStart(PlayerStartCarryEvent event){
        Player player = event.getPlayer();
        Entity target = event.getTarget();

        if(target instanceof Player){
            PlayerWrapper targetPw = PlayerManager.get(((Player)target));
            PlayerRole targetRole = targetPw.getRole();

            PlayerWrapper playerPw = PlayerManager.get(player);
            PlayerRole playerRole = playerPw.getRole();

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

        if(target instanceof Sheep){
            player.sendMessage("Sheep");
        }
    }
}
