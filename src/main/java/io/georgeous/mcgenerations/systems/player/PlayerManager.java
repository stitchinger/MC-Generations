package io.georgeous.mcgenerations.systems.player;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerManager {
    private static final HashMap<UUID, PlayerWrapper> playersMap = new HashMap<>();

    public static PlayerData data = PlayerData.getInstance();
    private static PlayerManager instance;

    private PlayerManager() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::initPlayer);
    }

    public static PlayerManager getInstance() {
        if (instance == null)
            instance = new PlayerManager();
        return instance;
    }

    public void destroy() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            if (get(player) != null) {
                data.savePlayer(get(player));
            } else {
                MCG.getInstance().getLogger().log(Level.SEVERE, "Player without wrapper: " + player);
            }
        });
    }

    public void initPlayer(Player player) {
        attachWrapperToPlayer(player);
        if (data.playerDataExists(player)) { // restore player
            data.restorePlayerWrapperFromConfig(get(player));
        }
    }

    public void attachWrapperToPlayer(Player player) {
        UUID uuid = player.getUniqueId();

        PlayerWrapper wrapper = new PlayerWrapper(player);
        playersMap.put(uuid, wrapper);
    }

    public PlayerWrapper get(Player player) {
        return get(player.getUniqueId());
    }

    public PlayerWrapper get(UUID uuid) {
        return playersMap.get(uuid);
    }

    public void add(Player player) {
        attachWrapperToPlayer(player);
    }

    public void remove(Player player) {
        PlayerWrapper playerWrapper = get(player);
        if (playerWrapper == null)
            return;

        data.savePlayer(playerWrapper);
        playersMap.remove(player.getUniqueId());
    }
}