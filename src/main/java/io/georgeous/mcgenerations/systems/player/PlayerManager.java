package io.georgeous.mcgenerations.systems.player;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class PlayerManager {
    private static HashMap<String, PlayerWrapper> playersMap = new HashMap<>();
    public static PlayerData data;

    public static void enable() {
        data = new PlayerData();
        for (Player player : getServer().getOnlinePlayers()) {
            initPlayer(player);
        }
        getServer().getPluginManager().registerEvents(new PlayerListener(), MCG.getInstance());
    }

    public static void disable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (get(player) != null) {
                PlayerWrapper wrap = get(player);
                data.savePlayer(wrap);
            } else {
                System.out.println("PLAYER WITHOUT WRAPPER");
            }
        }
    }

    public static void initPlayer(Player player) {
        attachWrapperToPlayer(player);
        if (data.playerDataExists(player)) { // restore player
            data.restorePlayerWrapperFromConfig(get(player));
        }
    }

    public static void attachWrapperToPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (playersMap.get(uuid) != null) {
            playersMap.put(uuid, null);
        }

        PlayerWrapper cp = new PlayerWrapper(player);
        playersMap.put(uuid, cp);
    }

    public static PlayerWrapper get(Player player) {
        return playersMap.get(player.getUniqueId().toString());
    }

    public static PlayerWrapper get(String uuid) {
        return playersMap.get(uuid);
    }

    public static void add(Player player) {
        attachWrapperToPlayer(player);
    }

    public static void remove(Player player) {
        PlayerWrapper playerWrapper = get(player);
        if (playerWrapper == null)
            return;

        data.savePlayer(playerWrapper);
        playersMap.remove(player.getUniqueId().toString());
    }

    public static void remove(String uuid) {
        playersMap.remove(uuid);
    }
}