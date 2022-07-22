package io.georgeous.mcgenerations.systems.role;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.utils.Logger;
import io.georgeous.mcgenerations.utils.NameManager;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.spicyhearts.SpicyAPI;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class RoleManager {
    private static final HashMap<UUID, PlayerRole> roles = new HashMap<>();

    private static RoleManager instance;

    private RoleManager() {
    }

    public static RoleManager get() {
        if (instance == null) {
            instance = new RoleManager();
        }
        return instance;
    }

    public void enable() {
        for (Player player : getServer().getOnlinePlayers()) {
            initPlayer(player);
        }
    }

    public void disable() {
        save();
    }

    public void update() {
        Iterator<Map.Entry<UUID, PlayerRole>> iterator = roles.entrySet().iterator();
        while (iterator.hasNext()) {
            PlayerRole role = iterator.next().getValue();

            if (!role.isOffline()) {
                role.update();
            } else {
                if (shouldDieOffline(role)) {
                    dropOfflineItems(role);
                    removeRoleData(role);
                    Logger.log(role.getName() + " died offline");
                    role.die();
                    iterator.remove();
                }
            }
        }
    }

    private boolean shouldDieOffline(PlayerRole role) {
        return role.getLastSeenOnline() + 1000L * McgConfig.getValidOfflineTime() < System.currentTimeMillis();
    }

    private void dropOfflineItems(PlayerRole role) {
        for (ItemStack content : role.getOfflineInventory().getContents()) {
            if (content != null) {
                Logger.log("dropped Items");
                role.getPlayer().getWorld().dropItemNaturally(role.getPlayer().getLocation(), content);
            }
        }
    }

    public PlayerRole get(Player player) {
        return get(player.getUniqueId());
    }

    public PlayerRole get(UUID uuid) {
        return roles.get(uuid);
    }

    public void initPlayer(Player player) {

        PlayerRole role = get(player.getUniqueId());

        if (role != null) {
            role.setPlayer(player);
            role.setIsOffline(false);
            role.refreshNick();
        } else if (roleDataExists(player)) { // restore player
            restoreRoleFromData(player);
        } else {

            resetToCouncil(player);

            PlayerWrapper pw = PlayerManager.get().getWrapper(player);
            if (pw != null)
                addToQueue(pw);
        }
    }

    private void resetToCouncil(Player player) {
        player.teleport(MCG.council.getRandomCouncilSpawn());
        // Reset Player
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.getActivePotionEffects().forEach(potionEffect -> {
            player.removePotionEffect(potionEffect.getType());
        });
        SpicyAPI.get().clearFoodList(player);
        player.setFoodLevel(20);
        player.setHealth(player.getMaxHealth());
    }

    private void addToQueue(PlayerWrapper pw) {

        Family family = pw.getLastFamily();
        if (family == null) return;

        if (!pw.getDiedOfOldAge()) return;

        family.getBabyQueue().add(pw.getPlayer());

        Notification.neutralMsg(pw.getPlayer(), "You have been added to the birthing queue of the " + family.getName() + " family");
    }

    public PlayerRole createAndAddRole(Player player, String name, int age, int gen, Family family) {
        PlayerRole playerRole = new PlayerRole(player, name, age, gen, family);
        roles.put(player.getUniqueId(), playerRole);
        ScoreboardHandler.get().refreshScoreboardOfPlayer(player);
        return playerRole;
    }

    public void removeRoleOfPlayer(Player player) {
        removeRoleOfPlayer(player.getUniqueId());
    }

    public void removeRoleOfPlayer(UUID uuid) {
        roles.remove(uuid);
    }

    public void save() {
        if (roles.isEmpty())
            return;
        roles.values().forEach(this::saveRoleData);
    }

    public void saveRoleData(PlayerRole playerRole) {
        FileConfiguration config = MCG.getInstance().getConfig();
        String uuid = playerRole.getPlayer().getUniqueId().toString();

        config.set("data.player." + uuid + ".role.name", playerRole.getName());
        config.set("data.player." + uuid + ".role.age", playerRole.getAgeManager().getAge());
        config.set("data.player." + uuid + ".role.familyname", playerRole.family.getName());
        config.set("data.player." + uuid + ".role.family", playerRole.family.getUuid());
        config.set("data.player." + uuid + ".role.generation", playerRole.generation);
        config.set("data.player." + uuid + ".role.mother", playerRole.getMothersName());
        config.set("data.player." + uuid + ".role.time", System.currentTimeMillis());
        config.set("data.player." + uuid + ".role.dead", playerRole.isDead);

        MCG.getInstance().saveConfig();
    }

    public void removeRoleData(PlayerRole playerRole) {
        FileConfiguration config = MCG.getInstance().getConfig();
        String uuid = playerRole.getPlayer().getUniqueId().toString();

        config.set("data.player." + uuid + ".role", null);

        MCG.getInstance().saveConfig();
    }

    public void restoreRoleFromData(Player player) {
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
        int gen = configSection.getInt("generation");
        String mother = configSection.getString("mother", null);

        // Family
        String familyUUID = configSection.getString("family");
        Family family = FamilyManager.getFamily(familyUUID);
        if (family == null) {
            Notification.errorMsg(player, "Error in role-restore. Family not found");
            family = FamilyManager.addFamily(NameManager.randomLast());
        }

        // CreateRole
        createAndAddRole(player, name, age, gen, family);

        RoleManager.get().get(player).setMothersName(mother);

        // delete Config entry after loaded
        //config.set("data.player." + uuid + ".role", null);
        MCG.getInstance().saveConfig();
    }

    public boolean roleDataExists(Player player) {
        return MCG.getInstance().getConfig().contains("data.player." + player.getUniqueId() + ".role");
    }

    public int getRoleCount() {
        return roles.size();
    }

}