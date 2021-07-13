package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.commands.*;
import io.georgeous.mcgenerations.files.DataManager;
import io.georgeous.mcgenerations.gadgets.*;
import io.georgeous.mcgenerations.listeners.*;
import io.georgeous.mcgenerations.manager.SurroManager;

import io.georgeous.mcgenerations.player.wrapper.PlayerManager;
import io.georgeous.mcgenerations.player.role.RoleManager;
import io.georgeous.mcgenerations.player.role.lifephase.listeners.PlayerPhaseUp;
import io.georgeous.mcgenerations.utils.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;

//import org.bukkit.scoreboard.Team;


public final class Main extends JavaPlugin {
    public static Main plugin;
    public DataManager data;

    public static World overworld;
    public static  Location councilLocation;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        printLoadupText();
        plugin = this;
        overworld = Bukkit.getWorld("mc-generations");
        councilLocation = new Location(overworld,0, 250, 0);
        this.saveDefaultConfig();
        this.data = new DataManager();

        registerEvents();
        registerCommands();

        PlayerManager.enable();
        RoleManager.enable();

        //startCouncil();
        Council council = new Council();

        for(Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()){
            //team.unregister();
        }


        // Start Update-Function
        new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    @Override
    public void onDisable() {
        SurroManager.disable();
        PlayerManager.disable();
        RoleManager.disable();
    }

    public void printLoadupText(){
        System.out.println("MCG ? ========== [ MC Generations ] ==========");
        System.out.println("MCG ? Version: 0.1");
        System.out.println("MCG ? Plugin by: Georgeous.io");
        System.out.println("MCG ? ========== [ MC Generations ] ==========");
    }

    private void update() {
        PlayerManager.update();
        RoleManager.update();
        SurroManager.update();
        //overworld.setTime(overworld.getTime() + 10);
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Interact(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new PlayerCarry(), this);
        getServer().getPluginManager().registerEvents(new PlayerPhaseUp(),this);
        // Manager
        getServer().getPluginManager().registerEvents(new PetManager(), this);
        getServer().getPluginManager().registerEvents(new SurroManager(), this);

    }

    public void registerCommands() {
        getServer().getPluginCommand("nick").setExecutor(new NickCommand());
        getServer().getPluginCommand("age").setExecutor(new Age(this));
        getServer().getPluginCommand("you").setExecutor(new YouAre(this));
        getServer().getPluginCommand("me").setExecutor(new Me(this));
        getServer().getPluginCommand("renamefamily").setExecutor(new RenameFamilyCommand());
        getServer().getPluginCommand("secinyear").setExecutor(new SecInYear());
        getServer().getPluginCommand("debug").setExecutor(new Debug());
        getServer().getPluginCommand("pets").setExecutor(new PetCommand());

        getCommand("debug").setTabCompleter(new TabCompletion());
        getCommand("pets").setTabCompleter(new TabComplete());
    }

    /*
    public void startCouncil(){
        // Spawn Gods if necessary
        Villager v = (Villager) overworld.spawnEntity(player.getLocation(), EntityType.VILLAGER);
        v = prepareSurro(v);
        v.setCustomName(name);

        new BukkitRunnable() {
            @Override
            public void run() {
                councilNoises();
            }
        }.runTaskTimer(this, 0L, 20L);
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

            int rand = Util.getRandom(sounds.length);
            world.playSound(loc, sounds[rand],1,0.1f);
        }
    }

     */

    // restore Inventory
    /*
    public void saveInvs() {
        for (Map.Entry<String, ItemStack[]> entry : menus.entrySet()) {
            this.getConfig().set("data.vaults." + entry.getKey(), entry.getValue());
        }
        this.saveConfig();
    }

    public void restoreInvs() {
        this.getConfig().getConfigurationSection("data.vaults").getKeys(false).forEach(key -> {
            @SuppressWarnings("unchecked")
            ItemStack[] content = ((List<ItemStack>) this.getConfig().get("data.vaults." + key)).toArray(new ItemStack[0]);
            menus.put(key, content);
        });
    }

     */

}


