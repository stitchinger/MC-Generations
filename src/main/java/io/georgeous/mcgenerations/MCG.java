package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.commands.*;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.files.DataManager;
import io.georgeous.mcgenerations.systems.pets.*;
import io.georgeous.mcgenerations.listeners.*;
import io.georgeous.mcgenerations.systems.surrogate.SurroManager;

import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.role.RoleManager;
import io.georgeous.mcgenerations.role.lifephase.listeners.PlayerPhaseUp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

//import org.bukkit.scoreboard.Team;


public final class MCG extends JavaPlugin {
    public static MCG plugin;
    public DataManager data;

    public PetManager petManager;

    public static World overworld;
    public static  Location councilLocation;

    public static MCG getInstance() {
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

        petManager = new PetManager(this);

        SurroManager.enable();
        PlayerManager.enable();
        FamilyManager.enable();
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
        petManager.disable();
        SurroManager.disable();
        PlayerManager.disable();
        RoleManager.disable();
        FamilyManager.disable();
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
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Interact(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerCarry(), this);
        getServer().getPluginManager().registerEvents(new PlayerPhaseUp(),this);
    }

    public void registerCommands() {
        getServer().getPluginCommand("gm").setExecutor(new GamemodeCommand());
        getServer().getPluginCommand("nick").setExecutor(new NickCommand());

        getServer().getPluginCommand("debug").setExecutor(new DebugCommand());
        getCommand("debug").setTabCompleter(new DebugCommandCompleter());
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


