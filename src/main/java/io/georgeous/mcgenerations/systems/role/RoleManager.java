package io.georgeous.mcgenerations.systems.role;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.commands.RoleCommand;
import io.georgeous.mcgenerations.utils.NameGenerator;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class RoleManager {
    private static HashMap<String, PlayerRole> roles = new HashMap<>();
    private static final long VALID_OFFLINE_TIME_SEC = 999999999999999999L;

    public static void enable() {
        registerCommands();
        getServer().getPluginManager().registerEvents(new RoleListener(), MCG.getInstance());
        for (Player player : getServer().getOnlinePlayers()) {
            initPlayer(player);
        }
    }

    public static void disable() {
        save();
    }

    public static void update() {
        for (Map.Entry<String, PlayerRole> entry : roles.entrySet()) {
            PlayerRole playerRole = entry.getValue();
            playerRole.update();
        }
    }

    private static void registerCommands() {
        RoleCommand roleCommand = new RoleCommand();
        getServer().getPluginCommand("role").setExecutor(roleCommand);
        MCG.getInstance().getCommand("role").setTabCompleter(roleCommand);
    }

    public static PlayerRole get(Player player) {
        String uuid = player.getUniqueId().toString();
        return get(uuid);
    }

    public static PlayerRole get(String uuid) {
        return roles.get(uuid);
    }

    public static void initPlayer(Player player) {
        boolean isValid = PlayerManager.get(player).getLastOfflineTime() < (VALID_OFFLINE_TIME_SEC * 1000);

        if (playerDataExists(player) && isValid) { // restore player
            restoreRole(player);
        } else {
            SpawnManager.spawnPlayer(player);
        }
    }

    public static PlayerRole createAndAddRole(Player player, String name, int age, Family family) {
        String uuid = player.getUniqueId().toString();

        // Overwrites players previous role
        if (roles.get(uuid) != null) {
            roles.put(uuid, null);
        }

        PlayerRole playerRole = new PlayerRole(player, name, age, family);
        roles.put(uuid, playerRole);
        return playerRole;
    }

    public static void remove(Player player) {
        String uuid = player.getUniqueId().toString();
        remove(uuid);
    }

    public static void remove(String uuid) {
        roles.remove(uuid);
    }

    public static void save() {
        if (roles.isEmpty())
            return;
        for (Map.Entry<String, PlayerRole> entry : roles.entrySet()) {
            if (entry.getValue() != null) {
                saveRole(entry.getValue());
            }
        }
    }

    public static void saveRole(PlayerRole playerRole) {
        FileConfiguration config = MCG.getInstance().getConfig();
        String uuid = playerRole.getPlayer().getUniqueId().toString();

        config.set("data.player." + uuid + ".role.name", playerRole.getName());
        config.set("data.player." + uuid + ".role.age", playerRole.am.getAge());
        config.set("data.player." + uuid + ".role.familyname", playerRole.family.getName());
        config.set("data.player." + uuid + ".role.family", playerRole.family.getUuid());
        config.set("data.player." + uuid + ".role.generation", playerRole.generation);
        config.set("data.player." + uuid + ".role.time", System.currentTimeMillis());

        MCG.getInstance().saveConfig();
    }

    public static void restoreRole(Player player) {
        String uuid = player.getUniqueId().toString();
        FileConfiguration config = MCG.getInstance().getConfig();
        ConfigurationSection configSection = config.getConfigurationSection("data.player." + uuid + ".role");

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

    public static boolean playerDataExists(Player player) {
        return MCG.getInstance().getConfig().contains("data.player." + player.getUniqueId().toString() + ".role");
    }

    public static int getRoleCount() {
        return roles.size();
    }
}