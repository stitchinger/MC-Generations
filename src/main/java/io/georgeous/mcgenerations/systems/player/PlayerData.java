package io.georgeous.mcgenerations.systems.player;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.utils.Logger;
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
        cs.set("rules_read", playerWrapper.getRulesRead());
        cs.set("rules_accepted", playerWrapper.getRulesAccepted());
        cs.set("name_preference", playerWrapper.getNamePreference());
        cs.set("last_family", playerWrapper.getLastFamily().getUuid());
        cs.set("old_age", playerWrapper.getDiedOfOldAge());

        plugin.saveConfig();
    }

    public void restoreFrom(PlayerWrapper playerWrapper, ConfigurationSection cs) {
        int lives = cs.getInt("lifes", 0);
        playerWrapper.setLifes(lives);

        double karma = cs.getDouble("karma", 0);
        playerWrapper.setKarma(karma);

        long playTime = cs.getLong("playtime", 0);
        playerWrapper.setPlayTime(playTime);

        long timeSinceOffline = cs.getLong("timesinceoffline", 999999L);
        playerWrapper.setLastOfflineTime(System.currentTimeMillis() - timeSinceOffline);

        boolean debugMode = cs.getBoolean("debug", false);
        playerWrapper.setDebugMode(debugMode);

        playerWrapper.setRulesRead(cs.getBoolean("rules_read", false));
        playerWrapper.setRulesAccepted(cs.getBoolean("rules_accepted", false));
        playerWrapper.setNamePreference(cs.getString("name_preference", "random"));

        playerWrapper.setDiedOfOldAge(cs.getBoolean("old_age", false));

        Family f = FamilyManager.getFamily(cs.getString("last_family", "empty"));

        if(f != null){
            playerWrapper.setLastFamily(f);
            Logger.log(f.getName() + "  dkljgaklöhjfdklöajfkldajfkdsl");
        } else{
            playerWrapper.setLastFamily(null);
        }

        Logger.log(playerWrapper.getDiedOfOldAge() + " OLD AGE?");

    }

    public void restorePlayerWrapperFromConfig(PlayerWrapper playerWrapper) {
        String uuid = playerWrapper.getPlayer().getUniqueId().toString();

        ConfigurationSection cs = config.getConfigurationSection(getPath(uuid));
        if (cs != null) {
            restoreFrom(playerWrapper, cs);
        }

    }

    public void deleteEntry(String uuid) {
        config.set(getPath(uuid), null);
        MCG.getInstance().saveConfig();
    }

    public boolean playerDataExists(Player player) {
        return MCG.getInstance().getConfig().contains(getPath(player.getUniqueId().toString()));
    }
}