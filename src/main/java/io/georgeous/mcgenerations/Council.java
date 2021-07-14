package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

public class Council {

    private final World world;
    private final double councilSoundChance = 0.25;
    private final Location endermanLocation;
    private final Location piglinLocation;
    private final Location villagerLocation;
    private final Location pillagerLocation;

    public Council(){
        world = MCG.overworld;
        endermanLocation = new Location(world, 0.5, 252, 11.5);
        piglinLocation = new Location(world, 0.5, 252, -10.5);
        pillagerLocation = new Location(world, 11.5, 252, 0.5);
        villagerLocation = new Location(world, -10.5, 252, 0.5);
        init();
    }

    public void init(){
        for(Entity entity : Bukkit.selectEntities(Bukkit.getConsoleSender(),"@e[tag=council]")){
            if(entity instanceof LivingEntity){
                ((LivingEntity) entity).setHealth(0);
            }
        }
        spawnEnderman();
        spawnPiglin();
        spawnPillager();
        spawnVillager();
        new BukkitRunnable() {
            @Override
            public void run() {
                councilNoises();
            }
        }.runTaskTimer(MCG.getInstance(), 0L, 20L);
    }

    public void spawnEnderman(){
        Minecart cart = (Minecart) world.spawnEntity(endermanLocation, EntityType.MINECART);
        cart.setInvulnerable(true);
        cart.addScoreboardTag("council");

        Enderman enderman = (Enderman) world.spawnEntity(endermanLocation, EntityType.ENDERMAN);
        enderman.setSilent(true);
        enderman.setInvulnerable(true);
        enderman.addScoreboardTag("council");

        cart.addPassenger(enderman);
    }

    public void spawnPiglin(){
        Cow cow = (Cow) world.spawnEntity(piglinLocation, EntityType.COW);
        cow.setInvulnerable(true);
        cow.setInvisible(true);
        cow.setSilent(true);
        cow.addScoreboardTag("council");

        PiglinBrute piglin = (PiglinBrute) world.spawnEntity(piglinLocation, EntityType.PIGLIN_BRUTE);
        piglin.setInvulnerable(true);
        piglin.setImmuneToZombification(true);
        piglin.setAdult();
        piglin.setSilent(true);
        piglin.addScoreboardTag("council");


        cow.addPassenger(piglin);
    }

    public void spawnPillager(){
        Cow cow = (Cow) world.spawnEntity(pillagerLocation, EntityType.COW);
        cow.setInvulnerable(true);
        cow.setInvisible(true);
        cow.setSilent(true);
        cow.addScoreboardTag("council");

        Illusioner illusioner = (Illusioner) world.spawnEntity(pillagerLocation, EntityType.ILLUSIONER);
        illusioner.setInvulnerable(true);
        illusioner.setSilent(true);
        illusioner.addScoreboardTag("council");
        illusioner.setAI(false);
        //illusioner.setRotation();
        //illusioner.setAware(false);
        //illusioner.setArrowCooldown(999999999);


        cow.addPassenger(illusioner);
    }

    public void spawnVillager(){
        Wolf wolf = (Wolf) world.spawnEntity(villagerLocation, EntityType.WOLF);
        wolf.setInvisible(true);
        wolf.setInvulnerable(true);
        wolf.setSilent(true);
        wolf.addScoreboardTag("council");

        Villager villager = (Villager) world.spawnEntity(villagerLocation, EntityType.VILLAGER);
        villager.setInvulnerable(true);
        villager.setSilent(true);
        villager.setAdult();
        villager.addScoreboardTag("council");

        wolf.addPassenger(villager);
    }

    public void councilNoises(){
        if(Math.random() > 1 - councilSoundChance){
            World world = Bukkit.getWorld("mc-generations");
            Location loc = new Location(world,0,250,0);

            Sound[] sounds = {
                    Sound.ENTITY_ENDERMAN_AMBIENT,
                    Sound.ENTITY_VILLAGER_AMBIENT,
                    Sound.ENTITY_ILLUSIONER_AMBIENT,
                    Sound.ENTITY_PIGLIN_AMBIENT
            };

            int rand = Util.getRandomInt(sounds.length);
            world.playSound(loc, sounds[rand],1,0.1f);
        }
    }
}
