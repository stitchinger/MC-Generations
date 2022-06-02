package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final RoleManager roleManager = RoleManager.get();
    private final PlayerManager playerManager = PlayerManager.get();

    @EventHandler(priority= EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerRole role = roleManager.get(player);
        if(role != null){
            removeFromFamily(role);
            roleManager.saveRoleData(role);
            roleManager.removeRoleOfPlayer(player);
        }

        PlayerWrapper wrapper = playerManager.getWrapper(player);
        if(wrapper != null){
            playerManager.remove(player);
        }

    }

    private void removeFromFamily(PlayerRole role) {
        role.getFamily().removeMember(role);
    }


}
