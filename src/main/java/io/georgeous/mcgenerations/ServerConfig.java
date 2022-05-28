package io.georgeous.mcgenerations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import static org.bukkit.Bukkit.getServer;

public class ServerConfig {

    private static ServerConfig instance;
    private FileConfiguration config;

    private ServerConfig() {
        config = MCG.getInstance().getConfig();
    }

    public static ServerConfig getInstance() {
        if (instance == null) {
            instance = new ServerConfig();
        }
        return instance;
    }

    public String getWorldName(){
        return Bukkit.getWorlds().get(0).getName();
    }

    public Location getCouncilLocation(){
        double x = config.getDouble("config.council.locationx");
        double y = config.getDouble("config.council.locationy");
        double z = config.getDouble("config.council.locationz");
        return new Location(MCG.overworld, x, y, z );
    }

    public Location getSpawnLocation(){
        double x = config.getDouble("config.spawncenterx");
        double z = config.getDouble("config.spawncenterz");
        return new Location(MCG.overworld, x, 0, z);
    }

    public double getSpawnRadius(){
        return config.getDouble("config.spawnradius");
    }

    public int getSecInLobby(){
        return config.getInt("config.timeinlobby");
    }
}
