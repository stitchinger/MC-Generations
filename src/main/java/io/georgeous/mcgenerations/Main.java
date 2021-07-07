package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.commands.*;
import io.georgeous.mcgenerations.family.FamilyManager;
import io.georgeous.mcgenerations.files.DataManager;
import io.georgeous.mcgenerations.gadgets.*;
import io.georgeous.mcgenerations.listeners.*;
import io.georgeous.mcgenerations.manager.SurroManager;

import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.utils.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.scoreboard.Team;


public final class Main extends JavaPlugin {
    public static Main plugin;
    public DataManager data;

    public static World overworld;
    public static  Location councilLocation;
    public static double councilSoundChance = 0.25;

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

        startCouncil();

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

    }

    public void printLoadupText(){
        System.out.println("MCG ? ========== [ MC Generations ] ==========");
        System.out.println("MCG ? Version: 0.1");
        System.out.println("MCG ? Plugin by: Georgeous.io");
        System.out.println("MCG ? ========== [ MC Generations ] ==========");
    }

    private void update() {
        PlayerManager.update();
        SurroManager.update();
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Interact(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new PlayerCarry(), this);
        // Manager
        getServer().getPluginManager().registerEvents(new PetManager(), this);
        getServer().getPluginManager().registerEvents(new SurroManager(), this);

    }

    public void registerCommands() {
        getServer().getPluginCommand("age").setExecutor(new Age(this));
        getServer().getPluginCommand("you").setExecutor(new YouAre(this));
        getServer().getPluginCommand("i").setExecutor(new Iam(this));
        getServer().getPluginCommand("me").setExecutor(new Me(this));
        getServer().getPluginCommand("skintest").setExecutor(new SkinTest());
        getServer().getPluginCommand("secinyear").setExecutor(new SecInYear());
        getServer().getPluginCommand("pv").setExecutor(new VaultCommand());
        getServer().getPluginCommand("debug").setExecutor(new Debug());
        getCommand("debug").setTabCompleter(new TabCompletion());
        getServer().getPluginCommand("pets").setExecutor(new PetCommand());
        getCommand("pets").setTabCompleter(new TabComplete());
    }

    public void startCouncil(){
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


