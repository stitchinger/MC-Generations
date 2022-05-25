package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final RoleManager roleManager = RoleManager.getInstance();
    PlayerManager playerManager = PlayerManager.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeMember(player);


        roleManager.saveRole(roleManager.get(player));
        roleManager.remove(player);

        playerManager.remove(event.getPlayer());
    }

    private void removeMember(Player player) {
        PlayerRole role = RoleManager.getInstance().get(player);
        if (role == null) {
            return;
        }

        role.getFamily().removeMember(role);
    }


}
