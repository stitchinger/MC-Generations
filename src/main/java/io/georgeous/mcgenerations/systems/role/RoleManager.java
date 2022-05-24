package io.georgeous.mcgenerations.systems.role;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.utils.NameGenerator;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class RoleManager {
    private static final HashMap<UUID, PlayerRole> roles = new HashMap<>();
    private static final long VALID_OFFLINE_TIME_SEC = Long.MAX_VALUE / 1000;

    private static RoleManager instance;

    private RoleManager() {
        for (Player player : getServer().getOnlinePlayers()) {
            initPlayer(player);
        }
    }

    public static RoleManager getInstance() {
        if (instance == null) {
            instance = new RoleManager();
        }
        return instance;
    }

    public void destroy() {
        save();
    }

    public void update() {
        roles.values().forEach(PlayerRole::update);
    }

    public PlayerRole get(Player player) {
        return get(player.getUniqueId());
    }

    public PlayerRole get(UUID uuid) {
        return roles.get(uuid);
    }

    public void initPlayer(Player player) {
        boolean isValid = PlayerManager.getInstance().getWrapper(player).getLastOfflineTime() < (VALID_OFFLINE_TIME_SEC * 1000);

        if (playerDataExists(player) && isValid) { // restore player
            restoreRole(player);
        } else {
            SpawnManager.spawnPlayer(player);
        }
    }

    public PlayerRole createAndAddRole(Player player, String name, int age, Family family) {
        PlayerRole playerRole = new PlayerRole(player, name, age, family);
        roles.put(player.getUniqueId(), playerRole);
        return playerRole;
    }

    public void remove(Player player) {
        remove(player.getUniqueId());
    }

    public void remove(UUID uuid) {
        roles.remove(uuid);
    }

    public void save() {
        if (roles.isEmpty())
            return;
        roles.values().forEach(this::saveRole);
    }

    public void saveRole(PlayerRole playerRole) {
        FileConfiguration config = MCG.getInstance().getConfig();
        String uuid = playerRole.getPlayer().getUniqueId().toString();

        config.set("data.player." + uuid + ".role.name", playerRole.getName());
        config.set("data.player." + uuid + ".role.age", playerRole.getAgeManager().getAge());
        config.set("data.player." + uuid + ".role.familyname", playerRole.family.getName());
        config.set("data.player." + uuid + ".role.family", playerRole.family.getUuid());
        config.set("data.player." + uuid + ".role.generation", playerRole.generation);
        config.set("data.player." + uuid + ".role.time", System.currentTimeMillis());

        MCG.getInstance().saveConfig();
    }

    public void restoreRole(Player player) {
        String uuid = player.getUniqueId().toString();
        FileConfiguration config = MCG.getInstance().getConfig();
        ConfigurationSection configSection = config.getConfigurationSection("data.player." + uuid + ".role");

        if (configSection == null) {
            // TODO: could not find player, recreate player
            return;
        }

        // PlayerRole
        int age = configSection.getInt("age");
        String name = configSection.getString("name");

        // Family
        String familyUUID = configSection.getString("family");
        Family family = FamilyManager.getFamily(familyUUID);
        if (family == null) {
            Notification.errorMsg(player, "Error in role-restore. Family not found");
            family = FamilyManager.addFamily(NameGenerator.randomLast());
        }

        // CreateRole
        createAndAddRole(player, name, age, family);

        // delete Config entry after loaded
        //config.set("data.player." + uuid + ".role", null);
        MCG.getInstance().saveConfig();
    }

    public boolean playerDataExists(Player player) {
        return MCG.getInstance().getConfig().contains("data.player." + player.getUniqueId() + ".role");
    }

    public int getRoleCount() {
        return roles.size();
    }

    public boolean isABaby(Player player) {
        PlayerRole role = get(player);
        if (role == null)
            return false;
        PhaseManager phaseManager = get(player).getPhaseManager();
        if (phaseManager == null)
            return false;
        return phaseManager.getCurrentPhase().getName().equalsIgnoreCase("baby");
    }
}