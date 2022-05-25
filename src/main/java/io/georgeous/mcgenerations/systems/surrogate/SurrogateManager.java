package io.georgeous.mcgenerations.systems.surrogate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class SurrogateManager implements Listener {

    public static HashMap<Player, Villager> playerSurrogateMap = new HashMap<>();

    public static SurrogateManager instance;

    public static SurrogateManager getInstance() {
        if (instance == null)
            instance = new SurrogateManager();
        return instance;
    }

    public void destroy() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            destroySurrogateOfPlayer(player);
        }
    }

    @Nullable
    public Villager getVillager(Player player) {
        return playerSurrogateMap.get(player);
    }

    public void update() {
        for (Map.Entry<Player, Villager> entry : playerSurrogateMap.entrySet()) {
            Player p = entry.getKey();
            Villager v = entry.getValue();

            if (!p.isOnline()) {
                destroySurrogateOfPlayer(p);
            } else {
                PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY, 10, 1, false, false, true);
                p.addPotionEffect(invis);
                Vector dir = p.getLocation().getDirection().setY(0).multiply(0);
                Location pos = p.getLocation().add(0, 0.0, 0);
                v.teleport(pos.add(dir));
            }
        }
    }

    public void create(Player player, String name) {
        if (playerSurrogateMap.get(player) != null) {
            destroySurrogateOfPlayer(player);
        }

        Villager v = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        prepareSurro(v);
        v.setCustomName(name);
        // Todo Add villager entity to nocollision team within players scoreboard

        playerSurrogateMap.put(player, v);
    }

    public void destroySurrogateOfPlayer(Player player) {
        if (playerSurrogateMap.get(player) != null) {
            Villager v = playerSurrogateMap.get(player);
            Location loc = new Location(v.getWorld(), v.getLocation().getX(), 200, v.getLocation().getZ());
            v.teleport(loc);
            v.setHealth(0);
            playerSurrogateMap.remove(player);
        }
    }

    private void prepareSurro(Villager v) {
        v.setAI(false);
        v.setCollidable(false);
        v.setAge(-1);
        v.setAgeLock(true);
        v.setCustomNameVisible(true);
        v.addScoreboardTag("surrogate");
        v.setInvulnerable(true);
        v.setSilent(true);
    }
}