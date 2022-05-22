package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getServer;

public class Council {

    private final World world;
    public final Location councilLocation;
    private final Location endermanLocation;
    private final Location piglinLocation;
    private final Location villagerLocation;
    private final Location pillagerLocation;
    private final double councilSoundChance = 0.25;

    public Council(World world) {
        this.world = world;
-         councilLocation = new Location(world, 666, 201, 666);
        endermanLocation = councilLocation.clone().add(0.5, 2, 11.5);
        piglinLocation = councilLocation.clone().add(0.5, 2, -10.5);
        pillagerLocation = councilLocation.clone().add(11.5, 2, 0.5);
        villagerLocation = councilLocation.clone().add(-10.5, 2, 0.5);

        init();
    }

    public void init() {

        for (Entity entity : Bukkit.selectEntities(Bukkit.getConsoleSender(), "@e[tag=council]")) {
            if (entity instanceof Minecart) {
                //entity.setInvulnerable(false);
                ((Minecart) entity).setInvulnerable(false);
                ((Minecart) entity).setDamage(9999);
                entity.remove();

            }
            if (entity instanceof LivingEntity) {
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

    public void spawnEnderman() {
        Minecart cart = (Minecart) world.spawnEntity(endermanLocation, EntityType.MINECART);
        cart.setInvulnerable(true);
        cart.addScoreboardTag("council");


        Enderman enderman = (Enderman) world.spawnEntity(endermanLocation, EntityType.ENDERMAN);
        enderman.setSilent(true);
        enderman.setInvulnerable(true);
        enderman.addScoreboardTag("council");

        cart.addPassenger(enderman);
    }

    public void spawnPiglin() {
        Cow cow = (Cow) world.spawnEntity(piglinLocation, EntityType.COW);
        cow.setInvulnerable(true);
        cow.setInvisible(false);
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

    public void spawnPillager() {
        Cow cow = (Cow) world.spawnEntity(pillagerLocation, EntityType.COW);
        cow.setInvulnerable(true);
        cow.setInvisible(false);
        cow.setSilent(true);
        cow.addScoreboardTag("council");

        Illusioner illusioner = (Illusioner) world.spawnEntity(pillagerLocation, EntityType.ILLUSIONER);
        illusioner.setInvulnerable(true);
        illusioner.setSilent(true);
        illusioner.addScoreboardTag("council");
        illusioner.setAI(false);
        cow.addPassenger(illusioner);
    }

    public void spawnVillager() {
        Wolf wolf = (Wolf) world.spawnEntity(villagerLocation, EntityType.WOLF);
        wolf.setInvisible(false);
        wolf.setInvulnerable(true);
        wolf.setSilent(true);
        wolf.setAI(false);
        wolf.addScoreboardTag("council");

        Villager villager = (Villager) world.spawnEntity(villagerLocation, EntityType.VILLAGER);
        villager.setInvulnerable(true);
        villager.setSilent(true);
        villager.setVillagerType(Villager.Type.JUNGLE);
        villager.setProfession(Villager.Profession.CLERIC);
        villager.setVillagerLevel(5);
        villager.setAdult();
        villager.addScoreboardTag("council");

        wolf.addPassenger(villager);
    }

    public void councilNoises() {
        if (Math.random() > 1 - councilSoundChance) {

            World world = MCG.overworld;

            Sound[] sounds = {
                    Sound.ENTITY_ENDERMAN_AMBIENT,
                    Sound.ENTITY_VILLAGER_AMBIENT,
                    Sound.ENTITY_ILLUSIONER_AMBIENT,
                    Sound.ENTITY_PIGLIN_AMBIENT
            };

            int rand = Util.getRandomInt(sounds.length);
            world.playSound(councilLocation, sounds[rand], 1, 0.1f);
        }
    }
}
