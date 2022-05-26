package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class Council {

    public final Location COUNCIL_LOCATION;
    private final World world;
    private final Location ENDERMAN_LOCATION;
    private final Location PIGLIN_LOCATION;
    private final Location VILLAGER_LOCATION;
    private final Location PILLAGER_LOCATION;
    private final double COUNCIL_SOUND_FREQUENCY = 0.25;

    public Council(World world) {
        this.world = world;
        COUNCIL_LOCATION = new Location(world, -6783, 264, -6011);

        ENDERMAN_LOCATION = COUNCIL_LOCATION.clone().add(0.5, 2, 11.5);
        PIGLIN_LOCATION = COUNCIL_LOCATION.clone().add(0.5, 2, -10.5);
        PILLAGER_LOCATION = COUNCIL_LOCATION.clone().add(11.5, 2, 0.5);
        VILLAGER_LOCATION = COUNCIL_LOCATION.clone().add(-10.5, 2, 0.5);

        init();
    }

    public void init() {

        // Remove old council entities before spawning new ones
        Bukkit.selectEntities(Bukkit.getConsoleSender(), "@e[tag=council]").forEach(entity -> {
            //MCG.getInstance().getLogger().log(Level.INFO, "Entity killed: " + entity.getType());
            if (entity instanceof Minecart) {
                entity.setInvulnerable(false);
                ((Minecart) entity).setDamage(9999);
                entity.remove();
            }
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setHealth(0);
            }
        });

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
        Minecart cart = (Minecart) world.spawnEntity(ENDERMAN_LOCATION, EntityType.MINECART);
        cart.setInvulnerable(true);
        cart.addScoreboardTag("council");

        Enderman enderman = (Enderman) world.spawnEntity(ENDERMAN_LOCATION, EntityType.ENDERMAN);
        enderman.setSilent(true);
        enderman.setInvulnerable(true);
        enderman.addScoreboardTag("council");

        cart.addPassenger(enderman);
    }

    public void spawnPiglin() {
        Cow cow = (Cow) world.spawnEntity(PIGLIN_LOCATION, EntityType.COW);
        cow.setInvulnerable(true);
        cow.setInvisible(false);
        cow.setSilent(true);
        cow.addScoreboardTag("council");

        PiglinBrute piglin = (PiglinBrute) world.spawnEntity(PIGLIN_LOCATION, EntityType.PIGLIN_BRUTE);
        piglin.setInvulnerable(true);
        piglin.setImmuneToZombification(true);
        piglin.setAdult();
        piglin.setSilent(true);
        piglin.addScoreboardTag("council");

        cow.addPassenger(piglin);
    }

    public void spawnPillager() {
        Cow cow = (Cow) world.spawnEntity(PILLAGER_LOCATION, EntityType.COW);
        cow.setInvulnerable(true);
        cow.setInvisible(false);
        cow.setSilent(true);
        cow.addScoreboardTag("council");

        Illusioner illusioner = (Illusioner) world.spawnEntity(PILLAGER_LOCATION, EntityType.ILLUSIONER);
        illusioner.setInvulnerable(true);
        illusioner.setSilent(true);
        illusioner.addScoreboardTag("council");
        illusioner.setAI(false);
        cow.addPassenger(illusioner);
    }

    public void spawnVillager() {
        Wolf wolf = (Wolf) world.spawnEntity(VILLAGER_LOCATION, EntityType.WOLF);
        wolf.setInvisible(false);
        wolf.setInvulnerable(true);
        wolf.setSilent(true);
        wolf.setAI(false);
        wolf.addScoreboardTag("council");

        Villager villager = (Villager) world.spawnEntity(VILLAGER_LOCATION, EntityType.VILLAGER);
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
        if (Math.random() > 1 - COUNCIL_SOUND_FREQUENCY) {
            World world = MCG.overworld;
            Sound[] sounds = {
                    Sound.ENTITY_ENDERMAN_AMBIENT,
                    Sound.ENTITY_VILLAGER_AMBIENT,
                    Sound.ENTITY_ILLUSIONER_AMBIENT,
                    Sound.ENTITY_PIGLIN_AMBIENT
            };
            int rand = Util.getRandomInt(sounds.length);
            world.playSound(COUNCIL_LOCATION, sounds[rand], 1, 0.1f);
        }
    }
}
