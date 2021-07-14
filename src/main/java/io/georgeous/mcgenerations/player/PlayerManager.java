package io.georgeous.mcgenerations.player;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {
    private static HashMap<String, PlayerWrapper> playersMap = new HashMap<>();

    public static void enable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            initPlayer(player);
        }
    }

    public static void disable() {
        saveAllPlayers();
    }

    public static void initPlayer(Player player){
        attachWrapperToPlayer(player);
        if (playerDataExists(player)) { // restore player
            restorePlayerWrapperFromConfig(get(player));
        }
    }

    public static void update() {
        for (Map.Entry<String, PlayerWrapper> entry : playersMap.entrySet()) {
            //PlayerWrapper playerWrapper = entry.getValue();
            //playerWrapper.update();
        }
    }

    public static boolean playerDataExists(Player player){
        return MCG.getInstance().getConfig().contains("data.player." + player.getUniqueId().toString() + ".wrapper");
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
        playersMap.remove(player.getUniqueId().toString());
    }

    public static void remove(String uuid) {
        playersMap.remove(uuid);
    }

    public static void saveAllPlayers() {
        if (playersMap.isEmpty())
            return;
        for (Map.Entry<String, PlayerWrapper> entry : playersMap.entrySet()) {
            Player player = Bukkit.getPlayer(UUID.fromString(entry.getKey()));
            savePlayer(player);
        }
    }

    public static void savePlayer(Player player) {
        FileConfiguration config = MCG.getInstance().getConfig();
        PlayerWrapper playerWrapper = get(player);
        String uuid = player.getUniqueId().toString();

        config.set("data.player." + uuid + ".wrapper", null);
        config.set("data.player." + uuid + ".wrapper.name", player.getName());
        config.set("data.player." + uuid + ".wrapper.karma", playerWrapper.getKarma());
        config.set("data.player." + uuid + ".wrapper.lives", playerWrapper.getLives());
        config.set("data.player." + uuid + ".wrapper.playtime", 696969);
        config.set("data.player." + uuid + ".wrapper.timesinceoffline", System.currentTimeMillis());

        MCG.getInstance().saveConfig();
    }

    public static void restorePlayerWrapperFromConfig(PlayerWrapper playerWrapper) {
        FileConfiguration c = MCG.getInstance().getConfig();
        Player player = playerWrapper.player;
        String uuid = player.getUniqueId().toString();

        playerWrapper.restoreFrom(c.getConfigurationSection("data.player." + uuid + ".wrapper"));

        // delete Config entry after loaded
        //c.set("data.player." + uuid + ".wrapper", null);
        MCG.getInstance().saveConfig();
    }

}
