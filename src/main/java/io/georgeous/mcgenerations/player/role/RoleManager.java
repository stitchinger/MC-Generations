package io.georgeous.mcgenerations.player.role;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.family.Family;
import io.georgeous.mcgenerations.family.FamilyManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class RoleManager {
    private static HashMap<String, PlayerRole> roles = new HashMap<>();

    public static void enable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            initPlayer(player);
        }
    }

    public static void disable() {
        saveAllRoles();
    }

    public static void update() {
        for (Map.Entry<String, PlayerRole> entry : roles.entrySet()) {
            PlayerRole playerRole = entry.getValue();
            playerRole.update();
        }
    }

    public static PlayerRole get(Player player) {
        return roles.get(player.getUniqueId().toString());
    }

    public static PlayerRole get(String uuid) {
        return roles.get(uuid);
    }

    public static void initPlayer(Player player){
        if (playerDataExists(player)) { // restore player
            restoreRoleFromConfig(player);
        } else {
            SpawnManager.spawnPlayer(player);
        }

    }

    public static PlayerRole createRole(Player player, String name, int age, Family family) {
        String uuid = player.getUniqueId().toString();
        if (roles.get(uuid) != null) {
            roles.put(uuid, null);
        }

        PlayerRole playerRole = new PlayerRole(player, name, age, family);
        roles.put(uuid, playerRole);
        return playerRole;
    }

    public static void remove(Player player) {
        roles.remove(player.getUniqueId().toString());
    }

    public static void remove(String uuid) {
        roles.remove(uuid);
    }

    public static void saveRole(PlayerRole playerRole) {
        FileConfiguration config = Main.getPlugin().getConfig();
        String uuid = playerRole.getPlayer().getUniqueId().toString();

        config.set("data.player." + uuid + ".role.name", playerRole.getName());
        config.set("data.player." + uuid + ".role.age", playerRole.am.getAge());
        config.set("data.player." + uuid + ".role.familyname", playerRole.family.getName());
        config.set("data.player." + uuid + ".role.family", playerRole.family.getUuid());
        config.set("data.player." + uuid + ".role.generation", playerRole.generation);
        config.set("data.player." + uuid + ".role.time", System.currentTimeMillis());

        Main.getPlugin().saveConfig();
    }

    public static void saveAllRoles() {
        if (roles.isEmpty())
            return;
        for (Map.Entry<String, PlayerRole> entry : roles.entrySet()) {
            if(entry.getValue() != null){
                saveRole(entry.getValue());
            }
        }
    }

    public static void restoreRoleFromConfig(Player player){
        String uuid = player.getUniqueId().toString();
        FileConfiguration config = Main.getPlugin().getConfig();
        ConfigurationSection configSection = config.getConfigurationSection("data.player." + uuid + ".role");

        // PlayerRole
        int age = configSection.getInt("age");
        String name = configSection.getString("name");
        String family = configSection.getString("familyname");

        createRole(player,name,age, FamilyManager.addFamily(family));

        // delete Config entry after loaded
        config.set("data.player." + uuid + ".role", null);
        Main.getPlugin().saveConfig();
    }



    public static boolean playerDataExists(Player player){
        return Main.getPlugin().getConfig().contains("data.player." + player.getUniqueId().toString() + ".role");
    }

}
