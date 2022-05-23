package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
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
    private static final int spawnCenterX = 20;
    private static final int spawnCenterY = -800;
    private static final int spawnRadius = 120;


    public static void spawnPlayer(Player player) {

        Notification.neutralMsg(player, "You will be reborn in " + timeInLobby + " seconds");
        NickAPI.refreshPlayer(player);
        GameMode playerGM = player.getGameMode();
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
        PlayerRole finalMom = findViableMother(player);
        boolean playerInDebug = PlayerManager.getInstance().get(player).isDebugMode();

        Bukkit.getScheduler().scheduleSyncDelayedTask(MCG.getInstance(), () -> {
            if (finalMom != null && !playerInDebug) {
                spawnAsBaby(player, finalMom);
            } else {
                spawnAsEve(player);
            }
            //player.setGameMode(GameMode.SURVIVAL);
            player.setGameMode(playerGM);
            player.setInvulnerable(false);
            PlayerManager.getInstance().get(player).setDiedOfOldAge(false);
            PlayerManager.getInstance().get(player).setLastBedLocation(null);

            new BukkitRunnable() {
                @Override
                public void run() {
                    Notification.neutralMsg(player, "Use [ §d/howto§r ] command to learn how to play.");
                }
            }.runTaskLater(MCG.getInstance(), 300);
        }, timeInLobby * 20L); // 20 Tick (1 Second) delay before run() is called
    }

    public static void spawnAsEve(Player player) {
        // If diedOfOldAge
        // Spawn at last bed
        Location lastBed = PlayerManager.getInstance().get(player).getLastBedLocation();
        boolean bedIsValid = false;
        if (lastBed != null) {
            bedIsValid = lastBed.distance(MCG.council.councilLocation) > 50;
        }

        if (PlayerManager.getInstance().get(player).getDiedOfOldAge() && PlayerManager.getInstance().get(player).getLastBedLocation() != null && bedIsValid) {
            player.teleport(PlayerManager.getInstance().get(player).getLastBedLocation());
        } else {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "spreadplayers " + spawnCenterX + " " + spawnCenterY + " 0 " + spawnRadius + " false " + player.getName());
        }


        String name = NameGenerator.randomFirst();
        Family family = FamilyManager.addFamily(NameGenerator.randomLast());
        RoleManager.getInstance().createAndAddRole(player, name, 10, family);

        player.setSaturation(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, SoundCategory.MASTER, 1, 1);
            }
        }.runTaskLater(MCG.getInstance(), 20);

        Notification.neutralMsg(player, "You were reincarnated as an Eve");
        Notification.neutralMsg(player, "Use [ §d/family rename Smith§r ] to rename your family");
    }

    public static void spawnAsBaby(Player newBorn, PlayerRole mother) {
        String name = NameGenerator.randomFirst();

        Family family = mother.getFamily();

        PlayerRole newBornRole = RoleManager.getInstance().createAndAddRole(newBorn, name, 0, family);
        family.addMember(newBornRole);

        mother.getMotherController().bornBaby(newBornRole);

        Notification.neutralMsg(newBorn, "You were reincarnated as a Baby");
        // Effects
        babyBornEffects(newBorn, mother.getPlayer());

        PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING, 100, 1, true, true, true);
        SurrogateManager.map.get(newBorn).addPotionEffect(glow);
    }

    public static PlayerRole findViableMother(Player child) {
        List<PlayerRole> viableMothers = new ArrayList<>();

        // find viable Mothers on Server
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerRole playerRole = RoleManager.getInstance().get(player);
            boolean playerHasRole
                    = RoleManager.getInstance().get(player) != null;
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