package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.manager.SurroManager;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.role.RoleManager;
import io.georgeous.mcgenerations.player.role.components.MotherController;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.management.relation.Role;


public class SpawnManager {

    private static int timeInLobby = 10; // in seconds

    public static void spawnPlayer(Player player){
        PlayerRole finalMom = findViableMother(player);
        player.sendMessage("You will be reborn in 10 seconds");

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if(finalMom != null){
                    spawnAsBaby(player, finalMom);
                }else{
                    spawnAsEve(player);
                }
            }
        }, timeInLobby * 10L); //20 Tick (1 Second) delay before run() is called
    }

    public static void spawnAsEve(Player player){
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"spreadplayers 0 0 200 10000 false " + player.getName());


        RoleManager.createRole(player);
        RoleManager.get(player).am.setAge(10);
        player.setSaturation(0);

        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE,1,1);
        player.sendMessage("You spawned as an Eve");
    }

    public static void spawnAsBaby(Player newBorn, PlayerRole mother){

        RoleManager.createRole(newBorn);
        PlayerRole playerRole = RoleManager.get(newBorn);

        newBorn.teleport(mother.getPlayer().getLocation().add(0,1,0));


        PlayerRole mothersRole = RoleManager.get((Player) mother);
        MotherController mc = mothersRole.mc;

        inheritFromMother(playerRole, mothersRole);

        mc.addChild(playerRole);

        // Messages
        newBorn.sendMessage("You spawned as a Baby");
        // Effects
        babyBornEffects(newBorn,mother.getPlayer());

        PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING,100,1,true,true,true);
        SurroManager.map.get(newBorn).addPotionEffect(glow);
    }

    public static PlayerRole findViableMother(Player child){
        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerRole m = RoleManager.get(player);
            boolean hasRole = RoleManager.get(player) != null;
            boolean notSelf = child != player;
            if(hasRole && notSelf){
                return m;
            }
        }
        return null;
    }

    public static void inheritFromMother(PlayerRole playerRole, PlayerRole mothersRole){
        playerRole.generation = mothersRole.generation + 1;
        playerRole.family = mothersRole.family;
    }

    private static void babyBornEffects(Player player, Entity mom){
        World world = player.getWorld();
        world.playSound(mom.getLocation(),Sound.EVENT_RAID_HORN,1,2);
        world.playSound(mom.getLocation(),Sound.ENTITY_PLAYER_SPLASH,1,1.5f);
        world.playSound(mom.getLocation(),Sound.ENTITY_PANDA_SNEEZE,2,1.5f);
        world.spawnParticle(Particle.FIREWORKS_SPARK,mom.getLocation(),50);
    }
}
