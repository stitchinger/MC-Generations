package io.georgeous.mcgenerations.player.role.lifephase.listeners;

import io.georgeous.mcgenerations.player.role.lifephase.events.PlayerPhaseUpEvent;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPhaseUp implements Listener {

    @EventHandler
    public void onPlayerPhaseUp(PlayerPhaseUpEvent event){
        String phaseName = event.getNewPhase().getName();
        Player player = event.getPlayer();
        if(phaseName.equalsIgnoreCase("baby")){
            player.getInventory().clear();
        }
        if(phaseName.equalsIgnoreCase("teen")){
            Util.giveItemIfNotInInventory(ItemManager.getBabyHandler(),player.getInventory());
        }
    }
}
