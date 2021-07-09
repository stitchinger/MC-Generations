package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import io.georgeous.mcgenerations.player.role.RoleManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerConnection implements Listener {
    private final Main main;

    public PlayerConnection() {
        this.main = Main.getPlugin();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        //PlayerManager.attachWrapperToPlayer(player);
        //event.setJoinMessage("Welcome to One Hour One Life!");
        //SpawnManager.spawnAsEve(player);
        //PlayerManager.get(player).setRole(new PlayerRole(player));
        PlayerManager.initPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerManager.savePlayer(player);
        PlayerManager.remove(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerRole playerRole = RoleManager.get(player);

        removeBabyHandlerFromDrops(event);

        if(playerRole != null){
            playerRole.die();
        }
        player.setBedSpawnLocation(Main.councilLocation, true);
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
