package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.player.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerConnection implements Listener {
    private final Main main;

    public PlayerConnection() {
        this.main = Main.getPlugin();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        /*
        if(PlayerManager.get(p) == null){
            PlayerManager.initPlayer(p);
        }
         */
        event.setJoinMessage("Welcome to One Hour One Life!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        //PlayerWrapper cp = PlayerManager.get(p);
        //PlayerManager.playersMap.remove(p);
        Bukkit.broadcastMessage(p.getName() + "Left the game");
        System.out.println(p.getName() + "Left the game");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player p = event.getEntity();
        PlayerWrapper playerWrapper = PlayerManager.get(p);
        PlayerRole playerRole = playerWrapper.getRole();

        removeBabyHandlerFromDrops(event);

        if(playerRole != null){
            playerRole.die();
        }

        //p.setBedSpawnLocation(new Location(p.getWorld(),0d,250d,0d), true);
        p.setBedSpawnLocation(Main.councilLocation, true);
    }

    public void removeBabyHandlerFromDrops(PlayerDeathEvent event){
        for(ItemStack item : event.getDrops()){
            if(item.hasItemMeta()){
                if(Objects.requireNonNull(item.getItemMeta()).hasDisplayName()){
                    if(item.getItemMeta().getDisplayName().contains("Baby-Handler")){
                        item.setAmount(0);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        PlayerWrapper playerWrapper = PlayerManager.get(p);

        SpawnManager.spawnPlayer(p,main);
    }



}
