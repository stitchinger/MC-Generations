package io.georgeous.mcgenerations.systems.role;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class RoleListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        RoleManager.initPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        RoleManager.saveRole(RoleManager.get(player));
        RoleManager.remove(player);
    }

    @EventHandler
    public void onRoleDead(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (RoleManager.get(player) == null) {
            return;
        }
        PlayerRole playerRole = RoleManager.get(player);

        removeBabyHandlerFromDrops(event);

        String roleName = playerRole.getName() + " " + playerRole.getFamily().getColoredName() + ChatColor.RESET;
        String msg = event.getDeathMessage().replace(player.getName(), roleName);
        event.setDeathMessage(msg);

        boolean diedOfOldAge = playerRole.am.getAge() >= 60;
        if (diedOfOldAge) {
            event.setDeathMessage(roleName + " died of old Age. RIP");
        }
        playerRole.die();

        player.setBedSpawnLocation(MCG.council.councilLocation, true);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        SpawnManager.spawnPlayer(player);
    }

    public void removeBabyHandlerFromDrops(PlayerDeathEvent event) {
        for (ItemStack item : event.getDrops()) {
            if (ItemManager.isBabyHandler(item)) {
                item.setAmount(0);
            }
        }
    }
}