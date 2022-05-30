package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.NameManager;
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

    private static final int timeInLobby = 10; // in seconds
    private static final int timeToHowtoNotification = 15;


    public static void spawnPlayer(Player playerToSpawn) {
        Notification.neutralMsg(playerToSpawn, "You will be reborn in " + timeInLobby + " seconds");
        NickAPI.refreshPlayer(playerToSpawn);

        GameMode playerGM = playerToSpawn.getGameMode();
        preparePlayerForLobby(playerToSpawn);

        PlayerRole finalMom = findViableMother(playerToSpawn);
        boolean playerToSpawnInDebugMode = PlayerManager.getInstance().getWrapper(playerToSpawn).isDebugMode();

        if (finalMom != null && !playerToSpawnInDebugMode) {
            Notification.neutralMsg(finalMom.getPlayer(), "You will get a baby in " + ServerConfig.getInstance().getSecInLobby() + " seconds");
        }


        Bukkit.getScheduler().scheduleSyncDelayedTask(MCG.getInstance(), () -> {
            if(!playerToSpawn.isOnline()){
                MCG.getInstance().getLogger().info("Player left right before spawning");
                return;
            }

            if (finalMom != null && !playerToSpawnInDebugMode && !finalMom.isDead) {
                spawnAsBaby(playerToSpawn, finalMom);
            } else {
                spawnAsEve(playerToSpawn);
            }

            resetPlayer(playerToSpawn, playerGM);

            new BukkitRunnable() {
                @Override
                public void run() {
                    Notification.neutralMsg(playerToSpawn, "Use [ §d/howto§r ] command to learn how to play.");
                }
            }.runTaskLater(MCG.getInstance(), 20L * timeToHowtoNotification);

        }, ServerConfig.getInstance().getSecInLobby() * 20L); // 20 Tick (1 Second) delay before run() is called
    }

    private static void resetPlayer(Player player, GameMode gmBefore){
        player.setGameMode(gmBefore);
        player.setInvulnerable(false);
        PlayerManager.getInstance().getWrapper(player).setDiedOfOldAge(false);
        PlayerManager.getInstance().getWrapper(player).setLastBedLocation(null);
    }

    private static void preparePlayerForLobby(Player player){
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
    }

    public static void spawnAsEve(Player player) {
        // If diedOfOldAge spawn at last bed
        Location lastBed = PlayerManager.getInstance().getWrapper(player).getLastBedLocation();
        boolean bedIsValid = false;
        if (lastBed != null) {
            bedIsValid = lastBed.distance(ServerConfig.getInstance().getCouncilLocation()) > 500;
            // Using the bedspawing for the council stuff
            // This makes sure, that the players bed isnt the council
            // This could happen, if the player never interacted with a bed
        }

        if (PlayerManager.getInstance().getWrapper(player).getDiedOfOldAge() &&
                PlayerManager.getInstance().getWrapper(player).getLastBedLocation() != null &&
                bedIsValid)
        {
            player.teleport(PlayerManager.getInstance().getWrapper(player).getLastBedLocation());
        } else {
            Location loc = ServerConfig.getInstance().getSpawnLocation();
            double radius = ServerConfig.getInstance().getSpawnRadius();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "spreadplayers " + loc.getX() + " " + loc.getZ() + " 0 " + radius + " false " + player.getName());
        }

        String name = NameManager.randomFirst();
        Family family = FamilyManager.addFamily(NameManager.randomLast());
        RoleManager.getInstance().createAndAddRole(player, name, 10, 1, family);

        //player.setSaturation(0); too hard?

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
        String name = NameManager.randomFirst();

        Family family = mother.getFamily();

        PlayerRole newBornRole = RoleManager.getInstance().createAndAddRole(newBorn, name, 0, 1, family);
        family.addMember(newBornRole);

        mother.getMotherController().bornBaby(newBornRole);

        newBornRole.getFamily().setMaxGenerations(newBornRole.getGeneration());
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