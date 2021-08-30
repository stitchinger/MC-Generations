package io.georgeous.mcgenerations.systems.player;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {

    private final MCG plugin;
    private final FileConfiguration config;

    public PlayerData() {
        plugin = MCG.getInstance();
        config = plugin.getConfig();
    }

    private String getPath(String uuid) {
        return "data.player." + uuid + ".wrapper";
    }

    public void savePlayer(PlayerWrapper playerWrapper) {
        String uuid = playerWrapper.getPlayer().getUniqueId().toString();


        ConfigurationSection cs = config.createSection(getPath(uuid));
        cs.set("name", playerWrapper.getPlayer().getName());
        cs.set("karma", playerWrapper.getKarma());
        cs.set("lives", playerWrapper.getLives());
        cs.set("playtime", playerWrapper.getPlayTime());
        cs.set("timesinceoffline", System.currentTimeMillis());

        plugin.saveConfig();
    }

    public void restoreFrom(PlayerWrapper playerWrapper, ConfigurationSection cs) {
        int lives = cs.getInt("lives");
        playerWrapper.setLives(lives);

        double karma = cs.getDouble("karma");
        playerWrapper.setKarma(karma);

        long playTime = cs.getLong("playtime");
        playerWrapper.setPlayTime(playTime);

        long timeSinceOffline = cs.getLong("timesinceoffline");
        playerWrapper.setLastOfflineTime(System.currentTimeMillis() - timeSinceOffline);

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