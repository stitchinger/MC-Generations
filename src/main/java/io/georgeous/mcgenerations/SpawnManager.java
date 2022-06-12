package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.*;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpawnManager {

    private static final int timeToHowtoNotification = 15;
    private static SpawnManager instance;

    private SpawnManager(){

    }

    public static SpawnManager get(){
        if(instance == null){
            instance = new SpawnManager();
        }
        return  instance;
    }


    public void spawnPlayer(Player spawnedPlayer) {
        PotionEffect hover = new PotionEffect(PotionEffectType.LEVITATION, 200, 1, false, false, false);
        spawnedPlayer.addPotionEffect(hover);
        PlayerWrapper spawnedPlayerWrapper = PlayerManager.get().getWrapper(spawnedPlayer);
        if(spawnedPlayerWrapper.getIsSpawning()){
            return;
        }
        Notification.neutralMsg(spawnedPlayer, "You will be reborn in " + McgConfig.getSecInLobby() + " seconds");

        spawnedPlayerWrapper.setIsSpawning(true);

        // Effects
        spawnedPlayer.getWorld().playSound(spawnedPlayer.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.MASTER,1.5f,1f);
        spawnedPlayer.getWorld().spawnParticle(Particle.END_ROD, spawnedPlayer.getLocation().add(0,1,0), 60, 0.01, 0.05, 0.01);


        NickAPI.refreshPlayer(spawnedPlayer);


        PlayerRole chosenMotherRole = findViableMother(spawnedPlayer);
        boolean playerToSpawnInDebugMode = PlayerManager.get().getWrapper(spawnedPlayer).isDebugMode();

        // Mother gets notified of birth
        if (chosenMotherRole != null && !playerToSpawnInDebugMode) {
            chosenMotherRole.getMotherController().setReservedForBaby(true);
            Notification.neutralMsg(chosenMotherRole.getPlayer(), "You will get a baby in " + McgConfig.getSecInLobby() + " seconds");
        }

        SpawnTask spawnTask = new SpawnTask(spawnedPlayer, chosenMotherRole);
        spawnTask.runTaskLater(MCG.getInstance(), 20L * McgConfig.getSecInLobby());

    }

    private class SpawnTask extends BukkitRunnable{

        Player spawnedPlayer;
        PlayerWrapper spawnedPlayerWrapper;
        boolean playerToSpawnInDebugMode;
        PlayerRole chosenMotherRole;

        public SpawnTask(Player player, PlayerRole chosenMotherRole){
            this.spawnedPlayer = player;
            this.spawnedPlayerWrapper = PlayerManager.get().getWrapper(player);
            this.playerToSpawnInDebugMode = spawnedPlayerWrapper.isDebugMode();
            this.chosenMotherRole = chosenMotherRole;

        }

        @Override
        public void run() {
            resetPlayer(spawnedPlayerWrapper);
            boolean motherStillValid =
                    chosenMotherRole != null
                            && !chosenMotherRole.isDead
                            && chosenMotherRole.getPlayer().isOnline();

            if (motherStillValid && !playerToSpawnInDebugMode) {
                if(!spawnedPlayer.isOnline()){
                    Notification.neutralMsg(chosenMotherRole.getPlayer(), "The baby didn't make it. Sorry");
                    chosenMotherRole.getMotherController().setReservedForBaby(false);
                    return;
                }
                spawnAsBaby(spawnedPlayer, chosenMotherRole);
            } else {
                spawnAsEve(spawnedPlayer);
            }

            ScoreboardHandler.get().refreshScoreboardOfPlayer(spawnedPlayer);

            new BukkitRunnable() {
                @Override
                public void run() {
                    Notification.neutralMsg(spawnedPlayer, "Use [ §d/howto§r ] command to learn how to play.");
                }
            }.runTaskLater(MCG.getInstance(), 20L * timeToHowtoNotification);
        }
    }

    private static void resetPlayer(PlayerWrapper wrapper){
        // Reset Player
        wrapper.getPlayer().removePotionEffect(PotionEffectType.LEVITATION);
        wrapper.setIsSpawning(false);
        wrapper.setDiedOfOldAge(false);
        wrapper.setLastBedLocation(null);
        wrapper.getPlayer().setGameMode(GameMode.SURVIVAL);
        wrapper.getPlayer().setInvulnerable(false);
        wrapper.getPlayer().getInventory().clear();

        // Reset Advancements
        Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
        while (iterator.hasNext())
        {
            AdvancementProgress progress = wrapper.getPlayer().getAdvancementProgress(iterator.next());
            for (String criteria : progress.getAwardedCriteria()){
                progress.revokeCriteria(criteria);
            }

        }
    }

    public static void spawnAsEve(Player player) {
        // If diedOfOldAge spawn at last bed
        PlayerWrapper playerWrapper = PlayerManager.get().getWrapper(player);
        Location lastBed = playerWrapper.getLastBedLocation();
        boolean bedIsValid = false;
        if (lastBed != null) {
            bedIsValid = lastBed.distance(McgConfig.getCouncilLocation()) > 500;
            // Using the bedspawing for the council stuff
            // This makes sure, that the players bed isnt the council
            // This could happen, if the player never interacted with a bed
        }

        // Teleport Eve
        if (playerWrapper.getDiedOfOldAge() && playerWrapper.getLastBedLocation() != null && bedIsValid) {
            player.teleport(PlayerManager.get().getWrapper(player).getLastBedLocation());
        } else {
            randomEveSpawn(player);
        }

        String name = NameManager.randomFirst();
        Family family = FamilyManager.addFamily(NameManager.randomLast());
        RoleManager.get().createAndAddRole(player, name, 10, 1, family);

        equipEve(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, SoundCategory.MASTER, 1, 1);
            }
        }.runTaskLater(MCG.getInstance(), 20);

        Notification.neutralMsg(player, "You were reborn as an Eve");
        Notification.neutralMsg(player, "Use [ §d/family rename Smith§r ] to rename your family");
    }

    private static void randomEveSpawn(Player player){
        //Location loc = McgConfig.getSpawnLocation();
        Location loc = rotationSpawnResult();
        double radius = McgConfig.getSpawnRadius();
        String cmd = "spreadplayers " + loc.getX() + " " + loc.getZ() + " 0 " + radius + " false " + player.getName();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);

    }

    private static Location rotationSpawnResult(){
        Location rotationCenter = McgConfig.getSpawnRotationCenter();
        double rotationRadius = McgConfig.getSpawnRotationRadius();
        double time = System.currentTimeMillis() / 1000d / 60d / 60d;
        double radian =  (time % (2 * Math.PI)) - Math.PI;

        double x = Math.cos(radian);
        double z = Math.sin(radian);


        Vector dir = new Vector(x,0, z);
        rotationCenter.add(dir.multiply(rotationRadius));

        return rotationCenter;
    }

    public static void spawnAsBaby(Player newBorn, PlayerRole mother) {
        String name = NameManager.randomFirst();

        Family family = mother.getFamily();

        PlayerRole newBornRole = RoleManager.get().createAndAddRole(newBorn, name, 0, 1, family);
        family.addMember(newBornRole);

        mother.getMotherController().bornBaby(newBornRole);


        newBornRole.getFamily().setMaxGenerations(newBornRole.getGeneration());

        // Refresh the scoreboard of all family members
        // so that the generations are up to date
        newBornRole.getFamily().getMembers().forEach(member -> {
            ScoreboardHandler.get().refreshScoreboardOfPlayer(member.getPlayer());
        });


        Notification.neutralMsg(newBorn, "You were reincarnated as a Baby");
        // Effects
        babyBornEffects(newBorn, mother.getPlayer());

        new BukkitRunnable(){
            @Override
            public void run() {
                PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING, 100, 1, true, true, true);
                SurrogateManager.getInstance().getSurrogateOfPlayer(newBorn).getEntity().addPotionEffect(glow);
            }
        }.runTaskLater(MCG.getInstance(), 20L);

    }

    private static void equipEve(Player player){
        player.getInventory().addItem(ItemManager.getEveStarterSeeds());
        player.getInventory().addItem(ItemManager.getEveStarterFood());

        ItemStack armor = ItemManager.getEveStarterArmor();
        if(armor.getType().equals(Material.LEATHER_HELMET)){
            player.getInventory().setHelmet(armor);
        }
        if(armor.getType().equals(Material.LEATHER_CHESTPLATE)){
            player.getInventory().setChestplate(armor);
        }
        if(armor.getType().equals(Material.LEATHER_LEGGINGS)){
            player.getInventory().setLeggings(armor);
        }
        if(armor.getType().equals(Material.LEATHER_BOOTS)){
            player.getInventory().setBoots(armor);
        }
    }

    public static PlayerRole findViableMother(Player child) {
        List<PlayerRole> viableMothers = new ArrayList<>();
        Bukkit.getLogger().info("Looking for mother");
        // find viable Mothers on Server
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerRole playerRole = RoleManager.get().get(player);
            boolean playerHasRole
                    = RoleManager.get().get(player) != null;
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