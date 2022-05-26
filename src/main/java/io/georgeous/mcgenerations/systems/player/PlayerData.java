package io.georgeous.mcgenerations.systems.player;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {

    private static PlayerData instance;
    private final MCG plugin = MCG.getInstance();
    private final FileConfiguration config = plugin.getConfig();

    private PlayerData() {
    }

    public static PlayerData getInstance() {
        if (instance == null)
            instance = new PlayerData();
        return instance;
    }

    private String getPath(String uuid) {
        return "data.player." + uuid + ".wrapper";
    }

    public void savePlayer(PlayerWrapper playerWrapper) {
        String uuid = playerWrapper.getPlayer().getUniqueId().toString();

        ConfigurationSection cs = config.createSection(getPath(uuid));
        cs.set("name", playerWrapper.getPlayer().getName());
        cs.set("karma", playerWrapper.getKarma());
        cs.set("lifes", playerWrapper.getLifes());
        cs.set("playtime", playerWrapper.getPlayTime());
        cs.set("timesinceoffline", System.currentTimeMillis());
        cs.set("debug", playerWrapper.isDebugMode());

        plugin.saveConfig();
    }

    public void restoreFrom(PlayerWrapper playerWrapper, ConfigurationSection cs) {
        int lives = cs.getInt("lifes");
        playerWrapper.setLifes(lives);

        double karma = cs.getDouble("karma");
        playerWrapper.setKarma(karma);

        long playTime = cs.getLong("playtime");
        playerWrapper.setPlayTime(playTime);

        long timeSinceOffline = cs.getLong("timesinceoffline");
        playerWrapper.setLastOfflineTime(System.currentTimeMillis() - timeSinceOffline);

        boolean debugMode = cs.getBoolean("debug");
        playerWrapper.setDebugMode(debugMode);
    }

    public void restorePlayerWrapperFromConfig(PlayerWrapper playerWrapper) {
        String uuid = playerWrapper.getPlayer().getUniqueId().toString();

        ConfigurationSection cs = config.getConfigurationSection(getPath(uuid));
        if (cs != null) {
            restoreFrom(playerWrapper, cs);
        }

        deleteEntry(uuid);
    }

    public void deleteEntry(String uuid) {
        config.set(getPath(uuid), null);
        MCG.getInstance().saveConfig();
    }

    public boolean playerDataExists(Player player) {
        return MCG.getInstance().getConfig().contains(getPath(player.getUniqueId().toString()));
    }
}