package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.surrogate.SurroManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.NameGenerator;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.ArrayList;
import java.util.List;

public class SpawnManager {

    private static final int timeInLobby = 5; // in seconds

    public static void spawnPlayer(Player player) {

        Notification.neutralMsg(player, "You will be reborn in " + timeInLobby + " seconds");
        NickAPI.refreshPlayer(player);
        GameMode playerGM = player.getGameMode();
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
        PlayerRole finalMom = findViableMother(player);
        boolean playerInDebug = PlayerManager.get(player).isDebugMode();

        Bukkit.getScheduler().scheduleSyncDelayedTask(MCG.getInstance(), () -> {
            if (finalMom != null && !playerInDebug) {
                spawnAsBaby(player, finalMom);
            } else {
                spawnAsEve(player);
            }
            //player.setGameMode(GameMode.SURVIVAL);
            player.setGameMode(playerGM);
            player.setInvulnerable(false);
            PlayerManager.get(player).setDiedOfOldAge(false);
            PlayerManager.get(player).setLastBedLocation(null);
        }, timeInLobby * 20L); // 20 Tick (1 Second) delay before run() is called
    }

    public static void spawnAsEve(Player player) {
        // If diedOfOldAge
        // Spawn at last bed
        Location lastBed = PlayerManager.get(player).getLastBedLocation();
        boolean bedIsValid = false;
        if (lastBed != null) {
            bedIsValid = lastBed.distance(MCG.council.councilLocation) > 50;
        }

        if (PlayerManager.get(player).getDiedOfOldAge() && PlayerManager.get(player).getLastBedLocation() != null && bedIsValid) {
            player.teleport(PlayerManager.get(player).getLastBedLocation());
        } else {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "spreadplayers 780 460 50 1000 false " + player.getName());
        }


        String name = NameGenerator.randomFirst();
        Family family = FamilyManager.addFamily(NameGenerator.randomLast());
        PlayerRole playerRole = RoleManager.createAndAddRole(player, name, 10, family);
        //family.addMember(playerRole);

        player.setSaturation(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, SoundCategory.MASTER, 1, 1);
            }
        }.runTaskLater(MCG.getInstance(), 20);

        Notification.neutralMsg(player, "You were reincarnated as an Eve");
        Notification.neutralMsg(player, "Use [/family rename name] to rename your family");
    }

    public static void spawnAsBaby(Player newBorn, PlayerRole mother) {
        String name = NameGenerator.randomFirst();

        Family family = mother.getFamily();

        PlayerRole newBornRole = RoleManager.createAndAddRole(newBorn, name, 0, family);
        family.addMember(newBornRole);

        mother.getMotherController().bornBaby(newBornRole);

        Notification.neutralMsg(newBorn, "You were reincarnated as a Baby");
        // Effects
        babyBornEffects(newBorn, mother.getPlayer());

        PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING, 100, 1, true, true, true);
        SurroManager.map.get(newBorn).addPotionEffect(glow);
    }

    public static PlayerRole findViableMother(Player child) {
        List<PlayerRole> viableMothers = new ArrayList<>();

        // find viable Mothers on Server
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerRole playerRole = RoleManager.get(player);
            boolean playerHasRole
                    = RoleManager.get(player) != null;
            boolean notSelf
                    = child != player;
            if (playerHasRole && notSelf) {
                if (playerRole.getMotherController().canHaveBaby()) {
                    viableMothers.add(playerRole);
                }
            }
        }

        if (viableMothers.size() != 0) {
            // get random mother
            return viableMothers.get(Util.getRandomInt(viableMothers.size()));
        } else {
            return null;
        }
    }

    private static void babyBornEffects(Player player, Entity mom) {
        World world = player.getWorld();
        world.playSound(mom.getLocation(), Sound.EVENT_RAID_HORN, 1, 2);
        world.playSound(mom.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1.5f);
        world.playSound(mom.getLocation(), Sound.ENTITY_PANDA_SNEEZE, 2, 1.5f);
        world.spawnParticle(Particle.FIREWORKS_SPARK, mom.getLocation(), 50);
    }
}