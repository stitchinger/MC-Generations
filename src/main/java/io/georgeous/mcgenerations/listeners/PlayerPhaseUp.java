package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.events.PlayerPhaseUpEvent;
import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPhaseUp implements Listener {

    @EventHandler
    public void onPlayerPhaseUp(PlayerPhaseUpEvent event) {
        int phaseId = event.getNewPhase().getId();
        Player player = event.getPlayer();
        ScoreboardHandler.get().refreshScoreboardOfPlayer(player);

        if (phaseId == 0) {
            player.getInventory().clear();
        }
        if (phaseId >= 3 && phaseId < 5) {
            giveBabyHandler(player, phaseId);
        }
    }

    private void giveBabyHandler(Player player, int phaseId) {
        if (player.getInventory().firstEmpty() == -1) {
            Notification.errorMsg(player, "Inventory full! Use </babyhandler> to get a Baby-Handler");
        } else {
            Util.giveItemIfNotInInventory(ItemManager.createBabyHandler(), player.getInventory());
        }
    }
}