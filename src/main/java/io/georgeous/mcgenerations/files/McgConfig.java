package io.georgeous.mcgenerations.files;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class McgConfig {

    private static File file;
    private static FileConfiguration cfg;

    public static void setup(String path){
        file = new File(path,"mcg-config.yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cfg = YamlConfiguration.loadConfiguration(file);
        setupDefaults();
        cfg.options().copyDefaults(true);
        save();
    }

    public static FileConfiguration get(){
        return cfg;
    }

    public static void save(){
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload(){
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    private static void setupDefaults(){
        cfg.addDefault("spawn_timer_sec", 10);
        cfg.addDefault("eve_spawn_center_x", -2000);
        cfg.addDefault("eve_spawn_center_z", -3000);
        cfg.addDefault("eve_spawn_rotation_radius", 2000);
        cfg.addDefault("eve_spawn_rotation_speed", 8);
        cfg.addDefault("eve_spawn_x", 540);
        cfg.addDefault("eve_spawn_z", 1800);
        cfg.addDefault("eve_spawn_radius", 400);
        cfg.addDefault("council_x", -6782);
        cfg.addDefault("council_z", -6011);
        cfg.addDefault("council_y", 264);
        cfg.addDefault("council_noise_frequency", 0.25);
        cfg.addDefault("baby_cooldown", 180);
        cfg.addDefault("min_birth_age", 15);
        cfg.addDefault("max_birth_age", 45);
        cfg.addDefault("valid_offline_time_sec", 60);
        cfg.addDefault("chat_range", 100);
        cfg.addDefault("chat_alone_msg_time", 2);
        cfg.addDefault("chat_filter_profanity", true);
    }

    public static Location getCouncilLocation(){
        double x = cfg.getDouble("council_x");
        double y = cfg.getDouble("council_y");
        double z = cfg.getDouble("council_z");
        return new Location(MCG.overworld, x, y, z );
    }

    public static Location getSpawnLocation(){
        double x = cfg.getDouble("eve_spawn_x");
        double z = cfg.getDouble("eve_spawn_z");
        return new Location(MCG.overworld, x, 0, z);
    }

    public static Location getSpawnRotationCenter(){
        double x = cfg.getDouble("eve_spawn_center_x");
        double z = cfg.getDouble("eve_spawn_center_z");
        return new Location(MCG.overworld, x, 0, z);
    }

    public static double getSpawnRotationRadius(){
        return cfg.getDouble("eve_spawn_rotation_radius");
    }

    public static double getSpawnRadius(){
        return cfg.getDouble("eve_spawn_radius");
    }

    public static double getSpawnRotationSpeed(){
        return cfg.getDouble("eve_spawn_rotation_speed");
    }

    public static int getSecInLobby(){
        return cfg.getInt("spawn_timer_sec");
    }

    public static double getCouncilNoiseFrequency(){
        return cfg.getDouble("council_noise_frequency");
    }

    public static int getBabyCooldown(){
        return cfg.getInt("baby_cooldown");
    }

    public static int getMaxBirthAge(){
        return cfg.getInt("max_birth_age");
    }

    public static int getMinBirthAge(){
        return cfg.getInt("min_birth_age");
    }

    public static long getValidOfflineTime(){
        return cfg.getLong("valid_offline_time_sec");
    }

    public static double getChatRange(){
        return cfg.getDouble("chat_range");
    }

    public static int getChatAloneMsgTime(){
        return cfg.getInt("chat_alone_msg_time");
    }

    public static boolean getChatFilterProfanity(){
        return cfg.getBoolean("chat_filter_profanity");
    }
}
