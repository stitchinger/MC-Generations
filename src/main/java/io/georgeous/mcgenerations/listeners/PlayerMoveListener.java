package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.management.relation.Role;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo().distance(McgConfig.getCouncilLocation()) < 2) {
            // Steps into light and start life
            SpawnManager.get().spawnPlayer(player);
        }

        PlayerRole playerRole = RoleManager.get().get(player);

        if(playerRole == null){
            return;
        }

        if(playerRole.getAgeManager().getAge() >= 3){
            return;
        }

        Material type = event.getFrom().getBlock().getType();

        if (isBed(type)) {
            Location target = event.getTo().clone();
            target.setY(event.getFrom().getY());
            event.setTo(target);
        }
    }

    private boolean isBed(Material material) {
        Material[] beds = {
                Material.WHITE_BED, Material.BLACK_BED, Material.BLUE_BED, Material.RED_BED, Material.GREEN_BED, Material.YELLOW_BED,
                Material.BROWN_BED, Material.CYAN_BED, Material.MAGENTA_BED, Material.GRAY_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED,
                Material.LIME_BED, Material.ORANGE_BED, Material.PINK_BED, Material.PURPLE_BED
        };

        for (Material bed : beds) {
            if(material == bed){
                return true;
            }
        }
        return false;
    }


}