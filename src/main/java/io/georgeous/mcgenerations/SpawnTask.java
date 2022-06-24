package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

 public class SpawnTask extends BukkitRunnable {

    Player spawnedPlayer;
    PlayerWrapper spawnedPlayerWrapper;
    boolean playerToSpawnInDebugMode;
    PlayerRole chosenMotherRole;

    public SpawnTask(Player player, PlayerRole chosenMotherRole){
        this.spawnedPlayer = player;
        this.spawnedPlayerWrapper = PlayerManager.get().getWrapper(player);
        this.playerToSpawnInDebugMode = spawnedPlayerWrapper.isDebugMode();
        this.chosenMotherRole = chosenMotherRole;
    }

    @Override
    public void run() {

        boolean motherStillValid =
                chosenMotherRole != null
                        && !chosenMotherRole.isDead
                        && chosenMotherRole.getPlayer().isOnline();

        if (motherStillValid && !playerToSpawnInDebugMode) {
            if(!spawnedPlayer.isOnline()){
                Notification.neutralMsg(chosenMotherRole.getPlayer(), "The baby didn't make it. Sorry");
                chosenMotherRole.getMotherController().setReservedForBaby(false);
                return;
            }
            SpawnManager.spawnAsBaby(spawnedPlayer, chosenMotherRole);
        } else {
            SpawnManager.spawnAsEve(spawnedPlayer);
        }
        SpawnManager.resetPlayerOnLifeStart(spawnedPlayerWrapper);
        ScoreboardHandler.get().refreshScoreboardOfPlayer(spawnedPlayer);

        new BukkitRunnable() {
            @Override
            public void run() {
                Notification.neutralMsg(spawnedPlayer, "Use [ §d/howto§r ] command to learn how to play.");
            }
        }.runTaskLater(MCG.getInstance(), 20L * 10);
    }
}
