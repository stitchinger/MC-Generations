package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.role.PlayerRole;
import io.georgeous.mcgenerations.role.RoleManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerConnection implements Listener {
    private final MCG main;

    public PlayerConnection() {
        this.main = MCG.getInstance();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerManager.initPlayer(player);
        RoleManager.initPlayer(player);
        player.sendMessage("Welcome to One Hour One Life");
        player.sendMessage("Join the Discord - " + ChatColor.BLUE + ChatColor.UNDERLINE + "https://discord.gg/U262bxT4jh");
        event.setJoinMessage("");

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        RoleManager.saveRole(RoleManager.get(player));
        RoleManager.remove(player);

        PlayerManager.savePlayer(player);
        PlayerManager.remove(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerRole playerRole = RoleManager.get(player);

        removeBabyHandlerFromDrops(event);

        if(playerRole != null){
            String roleName = playerRole.getName() + " " + playerRole.getFamily().getColoredName() + ChatColor.RESET;
            String msg = event.getDeathMessage().replace(player.getName(), roleName);
            event.setDeathMessage(msg);
            if(playerRole.am.getAge() >= 60){
                event.setDeathMessage(roleName + " died of old Age. RIP");
            }
            playerRole.die();
        }
        player.setBedSpawnLocation(MCG.councilLocation, true);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        SpawnManager.spawnPlayer(player);
    }

    public void removeBabyHandlerFromDrops(PlayerDeathEvent event){
        for(ItemStack item : event.getDrops()){
            if(ItemManager.isBabyHandler(item)){
                item.setAmount(0);
            }
        }
    }


}
