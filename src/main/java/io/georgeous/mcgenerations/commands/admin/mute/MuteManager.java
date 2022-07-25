package io.georgeous.mcgenerations.commands.admin.mute;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.adoption.AdoptionRequest;
import io.georgeous.mcgenerations.utils.Logger;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MuteManager {

    private static final HashMap<UUID, Long> coolDown = new HashMap<>();
    private final int MUTE_TIME_MIN = 5;

    private static MuteManager instance;

    private MuteManager(){
        new BukkitRunnable(){
            @Override
            public void run() {
                Iterator<Map.Entry<UUID, Long>> iterator = coolDown.entrySet().iterator();
                while (iterator.hasNext()) {
                    Long endTime = iterator.next().getValue();

                    if(System.currentTimeMillis() > endTime){
                        Logger.log("removed mute entry");
                        iterator.remove();
                    }
                }
            }
        }.runTaskTimer(MCG.getInstance(), 0L, 20L * 60 * 5);
    }

    public static MuteManager get() {
        if (instance == null) {
            instance = new MuteManager();
        }
        return instance;
    }

    public void mute(Player player){
        Long timeOfUnmute = System.currentTimeMillis() + 1000L * 60 * MUTE_TIME_MIN;
        coolDown.put(player.getUniqueId(), timeOfUnmute);
        Notification.errorMsg(player, "You got muted by an admin for " + MUTE_TIME_MIN + " minutes");
    }

    public boolean isMuted(Player player) {
        Long timeOfUnmute = coolDown.get(player.getUniqueId());
        if (timeOfUnmute == null)
            return false;

        boolean timeIsUp = System.currentTimeMillis() > timeOfUnmute;
        if (timeIsUp) {
            coolDown.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public int getRemainingMuteTimeSec(Player player){
        Long timeOfUnmute = coolDown.get(player.getUniqueId());
        if (timeOfUnmute == null)
            return 0;

        return Math.max(0, (int)((timeOfUnmute - System.currentTimeMillis()) / 1000));
    }

}
