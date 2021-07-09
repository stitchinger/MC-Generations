package io.georgeous.mcgenerations.player;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.SpawnManager;
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
        } else {
            SpawnManager.spawnPlayer(player);  // Spawn player
        }
    }

    public static void update() {
        for (Map.Entry<String, PlayerWrapper> entry : playersMap.entrySet()) {
            // todo Offline countdown for offline players
            //PlayerWrapper playerWrapper = entry.getValue();
            //playerWrapper.update();
        }
    }

    public static boolean playerDataExists(Player player){
        return Main.getPlugin().getConfig().contains("data.player." + player.getUniqueId().toString());
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
        FileConfiguration config = Main.getPlugin().getConfig();
        PlayerWrapper playerWrapper = get(player);
        String uuid = player.getUniqueId().toString();



        config.set("data.player." + uuid, null);

        config.set("data.player." + uuid + ".karma", playerWrapper.getKarma());
        config.set("data.player." + uuid + ".lives", playerWrapper.getLives());
        config.set("data.player." + uuid + ".playtime", 696969);

        /*
        PlayerRole playerRole = playerWrapper.getRole();
        if (playerRole != null) {
            config.set("data.player." + uuid + ".role.name", playerRole.getName());
            config.set("data.player." + uuid + ".role.age", playerRole.am.ageInYears);
            config.set("data.player." + uuid + ".role.familyname", playerRole.family.getName());
            config.set("data.player." + uuid + ".role.family", playerRole.family.getUuid());
            config.set("data.player." + uuid + ".role.generation", playerRole.generation);
            config.set("data.player." + uuid + ".role.time", System.currentTimeMillis());
        }

         */
        Main.getPlugin().saveConfig();
    }

    public static void restorePlayerWrapperFromConfig(PlayerWrapper playerWrapper) {
        FileConfiguration c = Main.getPlugin().getConfig();
        Player player = playerWrapper.player;
        String uuid = player.getUniqueId().toString();

        playerWrapper.restoreFrom(c.getConfigurationSection("data.player." + uuid));

        // delete Config entry after loaded
        c.set("data.player." + uuid, null);
        Main.getPlugin().saveConfig();
    }

}
