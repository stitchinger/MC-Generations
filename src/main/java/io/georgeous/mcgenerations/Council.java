package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;


public class Council {

    private final World world;
    private final Location ENDERMAN_LOCATION;
    private final Location PIGLIN_LOCATION;
    private final Location VILLAGER_LOCATION;
    private final Location PILLAGER_LOCATION;
    private final double COUNCIL_SOUND_FREQUENCY = 0.25;

    public Council(World world) {
        this.world = world;
        Location COUNCIL_LOCATION = McgConfig.getCouncilLocation();

        ENDERMAN_LOCATION = COUNCIL_LOCATION.clone().add(9, 5, 9);
        PIGLIN_LOCATION = COUNCIL_LOCATION.clone().add(9, 5, -9);
        PILLAGER_LOCATION = COUNCIL_LOCATION.clone().add(-9, 5, 9);
        VILLAGER_LOCATION = COUNCIL_LOCATION.clone().add(-9, 5, -9);

        init();
    }

    public void init() {

        // Remove old council entities before spawning new ones
        Bukkit.selectEntities(Bukkit.getConsoleSender(), "@e[tag=council]").forEach(entity -> {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setHealth(0);
            }
        });

        spawnEnderman();
        spawnPiglin();
        spawnPillager();
        spawnVillager();
        spawnGui();
        new BukkitRunnable() {
            @Override
            public void run() {
                councilNoises();
            }
        }.runTaskTimer(MCG.getInstance(), 0L, 20L);
    }

    private void spawnEnderman() {
        Enderman enderman = (Enderman) world.spawnEntity(ENDERMAN_LOCATION, EntityType.ENDERMAN);
        enderman.setSilent(true);
        enderman.setInvulnerable(true);
        enderman.addScoreboardTag("council");
        enderman.setAI(false);
        enderman.setRemoveWhenFarAway(false);

        Location lookAt = McgConfig.getCouncilLocation();
        enderman.teleport(enderman.getLocation().setDirection(lookAt.subtract(enderman.getLocation()).toVector()));
    }

    private void spawnPiglin() {
        PiglinBrute piglin = (PiglinBrute) world.spawnEntity(PIGLIN_LOCATION, EntityType.PIGLIN_BRUTE);
        piglin.setInvulnerable(true);
        piglin.setImmuneToZombification(true);
        piglin.setAdult();
        piglin.setSilent(true);
        piglin.addScoreboardTag("council");
        piglin.setRemoveWhenFarAway(false);
        piglin.setAI(false);

        Location lookAt = McgConfig.getCouncilLocation();
        piglin.teleport(piglin.getLocation().setDirection(lookAt.subtract(piglin.getLocation()).toVector()));
    }

    private void spawnPillager() {
        Illusioner illusioner = (Illusioner) world.spawnEntity(PILLAGER_LOCATION, EntityType.ILLUSIONER);
        illusioner.setInvulnerable(true);
        illusioner.setSilent(true);
        illusioner.addScoreboardTag("council");
        illusioner.setAI(false);
        illusioner.setPersistent(true);
        illusioner.setRemoveWhenFarAway(false);

        Location lookAt = McgConfig.getCouncilLocation();
        illusioner.teleport(illusioner.getLocation().setDirection(lookAt.subtract(illusioner.getLocation()).toVector()));
    }

    private void spawnVillager() {
        Villager villager = (Villager) world.spawnEntity(VILLAGER_LOCATION, EntityType.VILLAGER);
        villager.setInvulnerable(true);
        villager.setSilent(true);
        villager.setVillagerType(Villager.Type.JUNGLE);
        villager.setProfession(Villager.Profession.CLERIC);
        villager.setVillagerLevel(5);
        villager.setAdult();
        villager.setAI(false);
        villager.addScoreboardTag("council");

        Location lookAt = McgConfig.getCouncilLocation();
        villager.teleport(villager.getLocation().setDirection(lookAt.subtract(villager.getLocation()).toVector()));
    }

    public void councilNoises() {
        if (Math.random() > 1 - McgConfig.getCouncilNoiseFrequency()) {
            World world = MCG.overworld;
            Sound[] sounds = {
                    Sound.ENTITY_ENDERMAN_AMBIENT,
                    Sound.ENTITY_VILLAGER_AMBIENT,
                    Sound.ENTITY_ILLUSIONER_AMBIENT,
                    Sound.ENTITY_PIGLIN_AMBIENT
            };
            int rand = Util.getRandomInt(sounds.length);
            world.playSound(McgConfig.getCouncilLocation(), sounds[rand], 1, 0.1f);
        }
    }

    private void spawnGui(){
        Location center = McgConfig.getCouncilLocation();
        float radius = 13.5f;
        float yOffset = -(1f);

        Location[] spawns = {
                center.clone().add(0, yOffset, radius),
                center.clone().add(0, yOffset, radius * (-1)),
                center.clone().add(radius, yOffset, 0),
                center.clone().add(radius * (-1), yOffset, 0)
        };

        for (Location spawn : spawns) {
            ArmorStand top = (ArmorStand) MCG.overworld.spawnEntity(spawn.clone().add(0,0.3,0), EntityType.ARMOR_STAND);
            top.setVisible(false);
            top.setMarker(true);
            top.setGravity(false);
            top.setCustomNameVisible(true);
            top.addScoreboardTag("council");
            top.setCustomName("Step into the light");

            ArmorStand bottom = (ArmorStand) MCG.overworld.spawnEntity(spawn, EntityType.ARMOR_STAND);
            bottom.setVisible(false);
            bottom.setMarker(true);
            bottom.setGravity(false);
            bottom.setCustomNameVisible(true);
            bottom.addScoreboardTag("council");
            bottom.setCustomName("to get reborn");
        }


    }

    public Location getRandomCouncilSpawn() {
        Location center = McgConfig.getCouncilLocation();
        float radius = 17;
        float yOffset = -1;

        Location[] spawns = {
                center.clone().add(0, yOffset, radius),
                center.clone().add(0, yOffset, radius * (-1)),
                center.clone().add(radius, yOffset, 0),
                center.clone().add(radius * (-1), yOffset, 0)
        };

        Random r = new Random();
        int randomNumber = r.nextInt(spawns.length);
        Location rnd = spawns[randomNumber];

        Location lookAt = center.clone().add(0,3,0);
        rnd.setDirection(lookAt.subtract(rnd).toVector());
        return rnd;
    }
}
