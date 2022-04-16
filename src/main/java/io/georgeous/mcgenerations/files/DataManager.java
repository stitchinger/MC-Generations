package io.georgeous.mcgenerations.files;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;

public class DataManager {
    private final MCG instance = MCG.getInstance();
    private FileConfiguration dataConfig = null;
    private final File dataFile = new File(this.instance.getDataFolder(), "data.yml");

    public void reloadDataConfig() {
        switch (createNewFile(dataFile)) {
            case -1:
                break;
            case 0:
                instance.getLogger().log(Level.WARNING, "Unable to create data.yml.");
            case 1:
                instance.getLogger().log(Level.INFO, "Data.yml was created successfully.");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
    }

    public FileConfiguration getDataConfig() {
        if (this.dataConfig == null)
            reloadDataConfig();
        return this.dataConfig;
    }

    /*public void saveConfig() {
        if (this.dataConfig == null)
            return;

        try {
            this.getConfig().save(this.dataFile);
        } catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "Could not save config to " + this.dataFile, e);
        }
    }*/

    private int createNewFile(File file) {
        if (!file.exists())
            try {
                return file.createNewFile() ? 1 : 0;
            } catch (Exception e) {
                return 0;
            }
        return -1;
    }
}