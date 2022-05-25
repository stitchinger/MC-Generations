package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.events.PlayerPhaseUpEvent;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPhaseUpListener implements Listener {

    @EventHandler
    public void onPlayerPhaseUp(PlayerPhaseUpEvent event) {
        String phaseName = event.getNewPhase().getName();
        int phaseId = event.getNewPhase().getId();
        Player player = event.getPlayer();
        if(phaseName.equalsIgnoreCase("baby")){
            player.getInventory().clear();
        }

        if(phaseId >= 3 && phaseId < 5){
            Util.giveItemIfNotInInventory(ItemManager.createBabyHandler(),player.getInventory());
        }
    }
}