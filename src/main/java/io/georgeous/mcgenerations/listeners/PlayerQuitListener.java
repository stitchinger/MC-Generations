package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.Logger;
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

        dealWithRole(player);
        dealWithWrapper(player);

        event.setQuitMessage("");
        SurrogateManager.getInstance().destroySurrogateOfPlayer(player);
    }

    private void dealWithWrapper(Player player){
        PlayerWrapper wrapper = playerManager.getWrapper(player);
        if(wrapper != null){
            if(wrapper.getLastFamily() != null){
                wrapper.getLastFamily().getBabyQueue().remove(wrapper.getPlayer());
            }
            playerManager.saveAndRemoveWrapper(player);
        }
    }

    private void dealWithRole(Player player){
        PlayerRole role = roleManager.get(player);


        if(role != null){
            //roleManager.saveRoleData(role);

            if(role.getFamily() != null){
                //removeFromFamily(role);
            }

            role.setOfflineInventory(player.getInventory());
            role.setIsOffline(true);
            Logger.log(role.getName() + " went missing.");
        }
    }

    private void removeFromFamily(PlayerRole role) {
        role.getFamily().removeMember(role);
    }


}
