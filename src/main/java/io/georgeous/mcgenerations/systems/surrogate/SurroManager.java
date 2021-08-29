package io.georgeous.mcgenerations.systems.surrogate;


import io.georgeous.mcgenerations.MCG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;


public class SurroManager implements Listener {

    public static HashMap<Player, Villager> map = new HashMap<>();

    public static void enable(){
        getServer().getPluginManager().registerEvents(new SurroListener(), MCG.getInstance());
    }

    public static void disable(){
        for(Player player : Bukkit.getOnlinePlayers()){
            destroy(player);
        }
    }

    public static void update(){

        for(Map.Entry<Player, Villager> entry : map.entrySet()) {
            Player p = entry.getKey();
            Villager v = entry.getValue();

            if(!p.isOnline()){
                destroy(p);
                continue;
            }else{
                PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY, 10, 1, false, false, true);
                p.addPotionEffect(invis);
                Vector dir = p.getLocation().getDirection().setY(0).multiply(0);
                Location pos = p.getLocation().add(0, 0.0, 0);
                v.teleport(pos.add(dir));
            }
        }
    }

    public static void create(Player player, String name) {
        if(map.get(player) != null){
            destroy(player);
        }

        Villager v = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        v = prepareSurro(v);
        v.setCustomName(name);

        map.put(player,v);
    }

    public static void destroy(Player player) {
        if(map.get(player) != null){
            Villager v = map.get(player);
            Location loc = new Location(v.getWorld(), v.getLocation().getX(), 200, v.getLocation().getZ());
            v.teleport(loc);
            v.setHealth(0);
            map.remove(player);
        }
    }

    private static Villager prepareSurro(Villager v){
        v.setAI(false);
        v.setCollidable(false);
        v.setAge(-1);
        v.setAgeLock(true);
        v.setCustomNameVisible(true);
        v.addScoreboardTag("surrogate");
        v.setInvulnerable(true);
        v.setSilent(true);

        getServer().dispatchCommand(Bukkit.getConsoleSender(), "team join nocollision @e[tag=surrogate,limit=3,sort=nearest,team=!nocollision]");
        return v;
    }

}


