package io.georgeous.mcgenerations.player.role;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class RoleManager {
    private static HashMap<String, PlayerRole> roles = new HashMap<>();

    public static void enable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            createRole(player);
        }
    }

    public static void disable() {
        //saveAllRoles();
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

    public static void createRole(Player player) {
        String uuid = player.getUniqueId().toString();
        if (roles.get(uuid) != null) {
            roles.put(uuid, null);
        }

        PlayerRole playerRole = new PlayerRole(player);
        roles.put(uuid, playerRole);
    }

    public static void saveRole(PlayerRole playerRole) {
        FileConfiguration config = Main.getPlugin().getConfig();
        String uuid = playerRole.getPlayer().getUniqueId().toString();

        config.set("data.player." + uuid + ".role.name", playerRole.getName());
        config.set("data.player." + uuid + ".role.age", playerRole.am.ageInYears);
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

}
