package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.player.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import io.georgeous.mcgenerations.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;



public class SpawnManager {

    private static int timeInLobby = 10; // in seconds

    public static void spawnPlayer(Player player, Main main){
        Entity finalMom = getMom(player);
        player.sendMessage("You will be reborn in 10 seconds");

        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                if(finalMom != null){
                    spawnAsBaby(player, finalMom, main);
                }else{
                    spawnAsEve(player, main);
                }

            }
        }, timeInLobby * 10L); //20 Tick (1 Second) delay before run() is called
    }

    public static void spawnAsEve(Player player, Main main){
        PlayerWrapper playerWrapper = PlayerManager.get(player);

        playerWrapper.setRole(new PlayerRole(player));

        //Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"spreadplayers 0 0 200 10000 false " + player.getName());

        playerWrapper.getRole().am.setAge(10);

        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE,1,1);
        player.sendMessage("You spawned as an Eve");
    }

    public static void spawnAsBaby(Player player, Entity mom, Main main){
        PlayerWrapper playerWrapper = PlayerManager.get(player);
        playerWrapper.setRole(new PlayerRole(player));

        player.teleport(mom.getLocation().add(0,1,0));

        inheritFromMother(playerWrapper, mom, main);

        playerWrapper.getRole().am.ageInYears = 0;
        playerWrapper.getRole().am.ageInSeconds = 0;

        PlayerManager.get((Player) mom).getRole().child = player;

        // Messages
        player.sendMessage("You spawned as a Baby");
        player.sendMessage(mom.getName() + ": Hello baby!");
        // Effects
        babyBornEffects(player,mom);
        PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING,100,1,true,true,true);
        player.addPotionEffect(glow);
    }

    public static Entity getMom(Player player){
        //List<Entity> moms = Bukkit.getServer().selectEntities(player,"@e[type=villager,sort=random,name=!Council]");
        List<Entity> moms = Bukkit.getServer().selectEntities(player,"@a[name=!"+player.getName()+", sort=random]");
        Entity mom = null;
        if(moms.size() > 0){
            mom = moms.get(0);
        }
        return mom;
    }

    public static void inheritFromMother(PlayerWrapper playerWrapper, Entity mom, Main main){
        PlayerWrapper cMom = PlayerManager.get((Player)mom);

        playerWrapper.getRole().generation = cMom.getRole().generation + 1;
        playerWrapper.getRole().family = cMom.getRole().family;

        //NameManager.name(cp.player, cp.firstName, cp.family.getName());
    }

    private static void babyBornEffects(Player player, Entity mom){
        World world = player.getWorld();
        world.playSound(mom.getLocation(),Sound.EVENT_RAID_HORN,1,2);
        world.playSound(mom.getLocation(),Sound.ENTITY_PLAYER_SPLASH,1,1.5f);
        world.playSound(mom.getLocation(),Sound.ENTITY_PANDA_SNEEZE,2,1.5f);
        world.spawnParticle(Particle.FIREWORKS_SPARK,mom.getLocation(),50);
    }
}
