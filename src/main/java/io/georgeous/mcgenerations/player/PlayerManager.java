package io.georgeous.mcgenerations.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    public static HashMap<String, PlayerWrapper> playersMap = new HashMap<>();

    public static void initPlayer(Player p) {
        String uuid = p.getUniqueId().toString();
        // Reset
        if (playersMap.get(uuid) != null) {
            playersMap.put(uuid, null);
        }
        PlayerWrapper cp = new PlayerWrapper(p);
        playersMap.put(uuid, cp);

        //cp.am.setAge(15);
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
        /*
        for (Map.Entry<String, CustomPlayer> entry : playersMap.entrySet()) {
            String name = entry.getValue().firstName;
            int age = entry.getValue().am.ageInYears;
            Main.getPlugin().getConfig().set("data.player." + entry.getKey() + ".age", age);
            Main.getPlugin().getConfig().set("data.player." + entry.getKey() + ".name", name);
        }
        Main.getPlugin().saveConfig();

         */
    }

    public static void restorePlayer() {
       /* Main.getPlugin().getConfig().getConfigurationSection("data.player").getKeys(false).forEach(key -> {
            @SuppressWarnings("unchecked")
            CustomPlayer cp = (CustomPlayer) Main.getPlugin().getConfig().get("data.player." + key);

            playersMap.put(key,cp);
        });

        */
    }

    public static void disable(){
        // todo
    }
}
