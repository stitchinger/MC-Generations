package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.commands.*;
import io.georgeous.mcgenerations.files.DataManager;
import io.georgeous.mcgenerations.files.FileManager;
import io.georgeous.mcgenerations.listeners.*;
import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.NameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.io.File;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;

public final class MCG extends JavaPlugin {
    public static MCG plugin;
    public static World overworld;
    public static Council council;
    public static long daySpeed = 2; // Default is 1
    public static int serverYear = 0;
    private static long lastServerTime; // in millis

    public DataManager data;

    private FileManager fileManager;
    private ScoreboardHandler scoreboardHandler;

    public static MCG getInstance() {
        return plugin;
    }


    @Override
    public void onEnable() {
        plugin = this;
        printLoadupText();
        this.saveDefaultConfig();
        //getConfig().options().copyDefaults(true);

        if(!(new File(this.getDataFolder().getPath() + "/scoreboard.yml").exists())) {
            this.saveResource("scoreboard.yml", false);
        }
        if(!(new File(this.getDataFolder().getPath() + "/playerdata.yml").exists())) {
            this.saveResource("playerdata.yml", false);
        }

        fileManager = new FileManager(this.getDataFolder().getPath());

        overworld = Bukkit.getWorld(ServerConfig.getInstance().getWorldName());

        getLogger().info("-------------------World name" + Bukkit.getWorlds().get(0).getName());
        council = new Council(overworld);
        getConfig().options().copyDefaults(false);


        this.data = new DataManager();

        PlayerManager.getInstance();
        FamilyManager.enable();
        RoleManager.getInstance();
        NameManager.loadConfig();

        registerEvents();
        registerCommands();

        makeBundleCraftable();

        overworld.setSpawnLocation(ServerConfig.getInstance().getCouncilLocation());

        getServer().dispatchCommand(Bukkit.getConsoleSender(), "veryspicy true");


        // Start Update-Function
        new BukkitRunnable() {
            @Override
            public void run() {
                update();

            }
        }.runTaskTimer(this, 0L, 1L);

        // Update Server Year
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                serverYear++;
            }
        }, 0L, 20L * 60);


        int year = getConfig().getInt("data.server.year");
        serverYear = year;

        saveConfig();

        scoreboardHandler = new ScoreboardHandler();
    }

    @Override
    public void onDisable() {
        SurrogateManager.getInstance().destroy();
        PlayerManager.getInstance().destroy();
        RoleManager.getInstance().destroy();
        FamilyManager.disable();
        NameManager.saveConfig();

        FileConfiguration config = getConfig();
        config.set("data.server.year", serverYear);
        saveConfig();


        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    public void printLoadupText() {
        getLogger().log(Level.FINE, "MCG ? ========== [ MC Generations ] ==========");
        getLogger().log(Level.FINE, "MCG ? Version: 0.1");
        getLogger().log(Level.FINE, "MCG ? Plugin by: Georgeous.io");
        getLogger().log(Level.FINE, "MCG ? ========== [ MC Generations ] ==========");
    }

    private void update() {
        RoleManager.getInstance().update();
        SurrogateManager.getInstance().update();

        //overworld.setTime(overworld.getTime() + daySpeed);
        // one day 24000
        // 20 ticks = 1 sec
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Interact(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerCarry(), this);
        getServer().getPluginManager().registerEvents(new PlayerPhaseUp(), this);
        getServer().getPluginManager().registerEvents(new CreatureSpawnListener(), this);
    }

    public void registerCommands() {
        registerCommand("gm", new GamemodeCommand());
        registerCommand("nick", new NickCommand());
        registerCommand("you", new YouAre());
        registerCommand("secinyear", new SecInYear());
        registerCommand("die", new DieCommand());
        registerCommand("dayspeed", new DaySpeedCommand());
        registerCommand("me", new MeCommand());
        registerCommand("debug", new DebugCommand());
        registerCommand("role", new RoleCommand());
        registerCommand("family", new FamilyCommand());
        registerCommand("howto", new HowtoCommand());
        registerCommand("discord", new DiscordCommand());

        registerCommand("msg", new CommandDeactivator());
        registerCommand("w", new CommandDeactivator());
        registerCommand("say", new CommandDeactivator());
    }

    private void registerCommand(String command, CommandExecutor executor) {
        PluginCommand pluginCommand = getServer().getPluginCommand(command);
        if (pluginCommand == null)
            return;
        pluginCommand.setExecutor(executor);
    }

    public void makeBundleCraftable() {
        ItemStack item = new ItemStack(Material.BUNDLE);
        NamespacedKey key = new NamespacedKey(this, "Bundle");

        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("SLS", "LAL", "LLL");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('A', Material.AIR);

        Bukkit.addRecipe(recipe);
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }
}



/*

Todo: NOcollision problem came back with surrogate
Todo: Council countdown beeping
Todo: Setup config file for ...
Todo: Show baby cooldown in info command
 */