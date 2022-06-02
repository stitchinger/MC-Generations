package io.georgeous.mcgenerations.systems.surrogate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class SurrogateManager implements Listener {

    public static HashMap<Player, SurrogateEntity> map = new HashMap<>();

    public static SurrogateManager instance;

    public static SurrogateManager getInstance() {
        if (instance == null)
            instance = new SurrogateManager();
        return instance;
    }

    public void destroyAllSurrogates() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            destroySurrogateOfPlayer(player);
        }
    }

    public void update() {
        for (Map.Entry<Player, SurrogateEntity> entry : map.entrySet()) {
            Player player = entry.getKey();
            SurrogateEntity surro = entry.getValue();

            if (!player.isOnline()) {
                destroySurrogateOfPlayer(player);
            } else {
                surro.update();
            }
        }
    }

    public SurrogateEntity getSurrogateOfPlayer(Player player) {
        return map.get(player);
    }

    public void createSurrogateForPlayer(Player player, String fullName) {
        if (map.get(player) != null) {
            destroySurrogateOfPlayer(player);
        }
        SurrogateEntity entity = new SurrogateEntity(player, fullName);
        map.put(player, entity);
    }

    public void destroySurrogateOfPlayer(Player player) {
        if (map.get(player) != null) {
            SurrogateEntity surrogateEntity = map.get(player);
            surrogateEntity.remove();
            map.remove(player);
        }
    }

}