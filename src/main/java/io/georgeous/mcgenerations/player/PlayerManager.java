package io.georgeous.mcgenerations.player;

import io.georgeous.mcgenerations.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {
    public static HashMap<String, PlayerWrapper> playersMap = new HashMap<>();

    public static void enable(){
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            PlayerManager.initPlayer(p);
        }
        //restorePlayer();
    }

    public static void disable(){

        //savePlayer();
    }

    public static void initPlayer(Player p) {
        String uuid = p.getUniqueId().toString();
        if (playersMap.get(uuid) == null) {
            PlayerWrapper cp = new PlayerWrapper(p);
            playersMap.put(uuid, cp);
        }
    }

    public static void update(){
        for(Map.Entry<String, PlayerWrapper> entry : playersMap.entrySet()) {
            PlayerWrapper playerWrapper = entry.getValue();
            playerWrapper.update();
            /*
            if (cp.playerRole.isDead) {
                PlayerManager.remove(entry.getKey());
            }
            */
        }
    }

    public static PlayerWrapper get(Player player){
        return playersMap.get(player.getUniqueId().toString());
    }

    public static PlayerWrapper get(String uuid){
        return playersMap.get(uuid);
    }

    public static void remove(Player player){
        playersMap.remove(player.getUniqueId().toString());
    }

    public static void remove(String uuid){
        playersMap.remove(uuid);
    }

    public static void saveAllPlayers() {
        if(playersMap.isEmpty())
            return;
        for (Map.Entry<String, PlayerWrapper> entry : playersMap.entrySet()) {
            Player player = Bukkit.getPlayer(UUID.fromString(entry.getKey()));
            savePlayer(player);
        }
    }

    public static void savePlayer(Player player){
        FileConfiguration config = Main.getPlugin().getConfig();
        PlayerWrapper playerWrapper = get(player);
        PlayerRole playerRole = playerWrapper.getRole();
        String uuid = player.getUniqueId().toString();

        config.set("data.player." + uuid + ".karma", playerWrapper.getKarma());
        config.set("data.player." + uuid + ".lives", playerWrapper.getLives());
        config.set("data.player." + uuid + ".playtime", 696969);
        config.set("data.player." + uuid + ".role.age", playerRole.am.ageInYears);
        config.set("data.player." + uuid + ".role.name", playerRole.getName());
        config.set("data.player." + uuid + ".role.familyname", playerRole.family.getName());
        config.set("data.player." + uuid + ".role.family", playerRole.family.getUuid());
        config.set("data.player." + uuid + ".role.generation", playerRole.generation);
        config.set("data.player." + uuid + ".role.time", System.currentTimeMillis());
        Main.getPlugin().saveConfig();
    }



    public static void restoreAllPlayers() {
        // Todo What if player is not online?
        Main.getPlugin().getConfig().getConfigurationSection("data.player").getKeys(false).forEach(key -> {
            //FileConfiguration c = Main.getPlugin().getConfig();

            PlayerWrapper playerWrapper = get(key);
            restorePlayer(playerWrapper);
        });
    }

    public static void restorePlayer(PlayerWrapper playerWrapper){
        FileConfiguration c = Main.getPlugin().getConfig();
        Player player = playerWrapper.player;
        String uuid = player.getUniqueId().toString();
        // Add new role
        playerWrapper.setRole(new PlayerRole(player));
        PlayerRole playerRole = playerWrapper.getRole();

        int lives = c.getInt("data.player." + uuid + ".lives");
        playerWrapper.setLives(lives);

        double karma = c.getDouble("data.player." + uuid + ".karma");
        playerWrapper.setKarma(karma);

        long playTime = c.getLong("data.player." + uuid + ".playtime");
        playerWrapper.setPlayTime(playTime);

        int age = c.getInt("data.player." + uuid + ".role.age");
        playerRole.am.setAge(age);

        String name = c.getString("data.player." + uuid + ".role.name");
        playerRole.setName(name);
    }
}
