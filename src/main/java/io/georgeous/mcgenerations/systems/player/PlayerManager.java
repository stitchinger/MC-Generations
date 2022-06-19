package io.georgeous.mcgenerations.systems.player;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerManager {
    private static final HashMap<UUID, PlayerWrapper> playersMap = new HashMap<>();

    public static PlayerData data = PlayerData.getInstance();
    private static PlayerManager instance;

    private PlayerManager(){};

    public static PlayerManager get() {
        if (instance == null)
            instance = new PlayerManager();
        return instance;
    }

    public void enable(){
        Bukkit.getServer().getOnlinePlayers().forEach(this::initPlayer);
    }

    public void disable() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            if (getWrapper(player) != null) {
                data.savePlayer(getWrapper(player));
            } else {
                MCG.getInstance().getLogger().log(Level.SEVERE, "Player without wrapper: " + player);
            }
        });
    }

    public void initPlayer(Player player) {
        attachWrapperToPlayer(player);
        if (data.playerDataExists(player)) { // restore player
            data.restorePlayerWrapperFromConfig(getWrapper(player));
            Logger.log("Restored Wrapper: " + player);
        }
    }

    public void attachWrapperToPlayer(Player player) {
        UUID uuid = player.getUniqueId();

        PlayerWrapper wrapper = new PlayerWrapper(player);
        playersMap.put(uuid, wrapper);
        Logger.log("Created wrapper : " + player.getName());
    }

    public Set<UUID> getWrapperAttachedPlayers() {
        return playersMap.keySet();
    }
    public PlayerWrapper getWrapper(Player player) {
        return getWrapper(player.getUniqueId());
    }

    public PlayerWrapper getWrapper(UUID uuid) {
        return playersMap.get(uuid);
    }

    public void add(Player player) {
        attachWrapperToPlayer(player);
    }

    public void saveAndRemoveWrapper(Player player) {
        PlayerWrapper playerWrapper = getWrapper(player);
        if (playerWrapper == null)
            return;

        data.savePlayer(playerWrapper);
        playersMap.remove(player.getUniqueId());
    }
}