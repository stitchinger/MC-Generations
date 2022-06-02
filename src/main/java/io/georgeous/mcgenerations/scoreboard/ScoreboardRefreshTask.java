package io.georgeous.mcgenerations.scoreboard;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ScoreboardRefreshTask extends BukkitRunnable {

    private final ScoreboardHandler handler;

    public ScoreboardRefreshTask(ScoreboardHandler handler) {
            this.handler = handler;
    }

    @Override
    public void run() {
        for(UUID player : PlayerManager.get().getWrapperAttachedPlayers()) {
            handler.refreshPlayer(Bukkit.getPlayer(player));
        }
    }
}
