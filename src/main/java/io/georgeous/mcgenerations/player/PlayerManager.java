package io.georgeous.mcgenerations.player;

import io.georgeous.mcgenerations.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    public static HashMap<String, PlayerWrapper> playersMap = new HashMap<>();

    public static void initPlayer(Player p) {
        String uuid = p.getUniqueId().toString();
        if (playersMap.get(uuid) == null) {
            PlayerWrapper cp = new PlayerWrapper(p);
            playersMap.put(uuid, cp);
        }
    }

    public static void update(){
        for(Map.Entry<String, PlayerWrapper> entry : playersMap.entrySet()) {
            PlayerWrapper cp = entry.getValue();
            cp.update();
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

    public static void savePlayer() {
        if(playersMap.isEmpty())
            return;
        for (Map.Entry<String, PlayerWrapper> entry : playersMap.entrySet()) {
            PlayerWrapper playerWrapper = entry.getValue();
            PlayerRole playerRole = playerWrapper.getRole();

            Main.getPlugin().getConfig().set("data.player." + entry.getKey() + ".karma", 696969);
            Main.getPlugin().getConfig().set("data.player." + entry.getKey() + ".role.age", playerRole.am.ageInYears);
            Main.getPlugin().getConfig().set("data.player." + entry.getKey() + ".role.name", playerRole.firstName);
            Main.getPlugin().getConfig().set("data.player." + entry.getKey() + ".role.generation", playerRole.generation);
            Main.getPlugin().getConfig().set("data.player." + entry.getKey() + ".role.time", System.currentTimeMillis());

        }
        Main.getPlugin().saveConfig();
    }

    public static void restorePlayer() {

        Main.getPlugin().getConfig().getConfigurationSection("data.player").getKeys(false).forEach(key -> {
            //PlayerWrapper playerWrapper = (PlayerWrapper) Main.getPlugin().getConfig().get("data.player." + key + ".object");
            PlayerWrapper playerWrapper = get(key);
            Player player = playerWrapper.player;
            playerWrapper.setRole(new PlayerRole(player));

            FileConfiguration c = Main.getPlugin().getConfig();

            int age = c.getInt("data.player." + key + ".role.age");
            playerWrapper.getRole().am.setAge(age);

            String name = c.getString("data.player." + key + ".role.name");
            playerWrapper.getRole().setName(name);
            player.sendMessage("test123");
            //playersMap.put(key,playerWrapper);
        });
    }

    public static void enable(){
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            PlayerManager.initPlayer(p);
        }
        //restorePlayer();
    }

    public static void disable(){
        //savePlayer();
    }
}
