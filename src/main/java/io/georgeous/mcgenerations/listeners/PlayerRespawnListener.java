package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.haoshoku.nick.api.NickAPI;


public class PlayerRespawnListener implements Listener {

    Player player = null;
    PlayerWrapper playerWrapper;

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        player = event.getPlayer();
        playerWrapper = PlayerManager.get().getWrapper(player);
        if(playerWrapper == null){
            PlayerManager.get().initPlayer(player);
            playerWrapper = PlayerManager.get().getWrapper(player);
        }
        playerWrapper.setLastGameMode(player.getGameMode());
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);

        new BukkitRunnable(){
            @Override
            public void run() {

                    NickAPI.resetNick(player);
                    NickAPI.resetSkin(player);
                    NickAPI.refreshPlayer(player);

            }
        }.runTaskLater(MCG.getInstance(), 20L);

        event.setRespawnLocation(MCG.council.getRandomCouncilSpawn());
    }




}
