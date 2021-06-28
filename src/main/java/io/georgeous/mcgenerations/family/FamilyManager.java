package io.georgeous.mcgenerations.family;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FamilyManager {
    private static HashMap<String, Family> families = new HashMap<>();

    public static void getFamily(String uuid){
        families.get(uuid);
    }

    public static Family addFamily(String name){
        Family f = new Family(name);
        families.put(f.getUuid(),f);
        return f;
    }

    public static Family addFamily(String name, String uuid){
        Family f = new Family(name);
        families.put(f.getUuid(),f);
        return f;
    }

    public static void saveAllFamilies(){
        for(Map.Entry<String, Family> entry : families.entrySet()) {
            Family family = entry.getValue();
            saveFamily(family);
        }
    }

    public static void saveFamily(Family family){
        FileConfiguration config = Main.getPlugin().getConfig();
        String uuid = family.getUuid();
        config.set("data.family." + uuid + ".name", family.getName());
        config.set("data.family." + uuid + ".established", family.getEstablished());
        config.set("data.family." + uuid + ".color", family.getColor());

        Main.getPlugin().saveConfig();
    }

    public static void restoreAllFamilies(){
        Main.getPlugin().getConfig().getConfigurationSection("data.family").getKeys(false).forEach(key -> {
            //Family family = addFamily();
            restoreFamily(key);
        });
    }

    public static void restoreFamily(String uuid){
        FileConfiguration c = Main.getPlugin().getConfig();

        //String uuid = family.getUniqueId().toString();

        String name = c.getString("data.family." + uuid + ".name");

        Family family = addFamily(name);

        family.setUuid(uuid);

        String color = c.getString("data.family." + uuid + ".color");
        family.setColor(color);



    }

}
