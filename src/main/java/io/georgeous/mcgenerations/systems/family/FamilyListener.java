package io.georgeous.mcgenerations.systems.family;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FamilyListener implements Listener {

    @EventHandler
    public void familyMemberDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        removeMember(player);
    }

    @EventHandler
    public void familyMemberQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        //removeMember(player);
    }

    private void removeMember(Player player) {
        PlayerRole role = RoleManager.get(player);
        if (role == null) {
            return;
        }

        role.getFamily().removeMember(role);
    }
}