package io.georgeous.mcgenerations.systems.family;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class FamilyManager {
    private static HashMap<String, Family> families = new HashMap<>();


    public static void enable(){
        registerCommands();
        registerEvents();

        restoreAllFamilies();
    }
    private static void registerCommands() {
        FamilyCommand familyCommand = new FamilyCommand();
        getServer().getPluginCommand("family").setExecutor(familyCommand);
        MCG.getInstance().getCommand("family").setTabCompleter(familyCommand);
    }

    private static void registerEvents(){
        getServer().getPluginManager().registerEvents(new FamilyListener(), MCG.getInstance());
    }

    public static void disable(){
        saveAllFamilies();
    }

    public static Family getFamily(String uuid){
        return families.get(uuid);
    }

    public static Family addFamily(String name){
        Family family = new Family(name);
        String uuid = family.getUuid();
        families.put(uuid, family);
        return family;
    }

    public static Family addFamily(String name, String uuid){
        Family family = new Family(name, uuid);
        families.put(uuid, family);
        return family;
    }


    public static void saveAllFamilies(){
        FileConfiguration config = MCG.getInstance().getConfig();
        config.set("data.family", null);
        for(Map.Entry<String, Family> entry : families.entrySet()) {
            Family family = entry.getValue();
            saveFamily(family);
        }

    }


    public static void saveFamily(Family family){
        if(family.isDead || family.memberCount() == 0){
            return;
        }
        FileConfiguration config = MCG.getInstance().getConfig();
        String uuid = family.getUuid();

        ConfigurationSection section = config.createSection("data.family." + uuid);
        section.set("name", family.getName());
        section.set("established", family.getEstablished());
        section.set("color", family.getColor());

        MCG.getInstance().saveConfig();
    }

    public static void restoreAllFamilies(){
        if(MCG.getInstance().getConfig().getConfigurationSection("data.family") == null){
            return;
        }
        MCG.getInstance().getConfig().getConfigurationSection("data.family").getKeys(false).forEach(key -> {
            restoreFamily(key);
        });
    }

    public static void restoreFamily(String uuid){
        FileConfiguration c = MCG.getInstance().getConfig();
        ConfigurationSection configSection = c.getConfigurationSection("data.family." + uuid);

        String name = configSection.getString("name");
        Family family = addFamily(name, uuid);

        String color = configSection.getString("color");
        family.setColor(color);

        Long established = configSection.getLong("established");
        family.setEstablished(established);

        // Delete FAmily Data
        //c.set("data.family." + uuid, null);
        MCG.getInstance().saveConfig();
    }

    public static List<Family> getAll(){
        List<Family> fs = new ArrayList<>();
        for (Map.Entry<String, Family> entry : families.entrySet()) {
            fs.add(entry.getValue());
        }
        return fs;
    }

}
