package io.georgeous.mcgenerations.files;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CFGFile {

    File dir;
    File file;
    YamlConfiguration cfg;

    public CFGFile(String ver, String name) {
        dir = new File(ver);
        file = new File(ver + name);
        if(dir.exists() == false) {
            dir.mkdirs();
        }
        if(file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void refresh() {
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void setValue(String location, Object o) {
        cfg.set(location, o);
        try {
            cfg.save(file);
        } catch (IOException e) {
            System.out.println("Hi");
        }
    }
    public int getInt(String path) {
        return cfg.getInt(path);
    }
    public long getLong(String path) {
        return cfg.getLong(path);
    }
    public String getString(String path) {
        return cfg.getString(path);
    }
    public Object getObject(String path) {
        return cfg.get(path);
    }
    public boolean getBoolean(String path) {
        return cfg.getBoolean(path);
    }
    public List<String> getStringList(String path){
        return cfg.getStringList(path);
    }
    public Set<String> getKeys(boolean deep){
        return cfg.getKeys(deep);
    }
    public ConfigurationSection getSection(String path){
        return cfg.getConfigurationSection(path);
    }
    public ConfigurationSection getDefaultSection(){
        return cfg.getDefaultSection();
    }
    public double getDouble(String path) {
        return cfg.getDouble(path);
    }
}

