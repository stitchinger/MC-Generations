package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.commands.*;
import io.georgeous.mcgenerations.files.DataManager;
import io.georgeous.mcgenerations.files.FileManager;
import io.georgeous.mcgenerations.listeners.*;
import io.georgeous.mcgenerations.listeners.backup.*;
import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import org.bukkit.*;
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
        printLoadupText();

        if(!(new File(this.getDataFolder().getPath() + "/scoreboard.yml").exists())) {
            this.saveResource("scoreboard.yml", false);
        }
        fileManager = new FileManager(this.getDataFolder().getPath());
        plugin = this;
        overworld = Bukkit.getWorld("FamilyCraftWorld");
        council = new Council(overworld);
        this.saveDefaultConfig();

        this.data = new DataManager();

        PlayerManager.getInstance();
        FamilyManager.enable();
        RoleManager.getInstance();

        registerEvents();
        registerCommands();

        setupServer();

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



        loadServerData();

        //MCG.getInstance().saveConfig();

        scoreboardHandler = new ScoreboardHandler();
    }

    @Override
    public void onDisable() {
        SurrogateManager.getInstance().destroy();
        PlayerManager.getInstance().destroy();
        RoleManager.getInstance().destroy();
        FamilyManager.disable();

        saveServerData();

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
        //overworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        // one day 24000
        // 20 ticks = 1 sec
    }

    public void registerEvents() {
        //getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);



        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Interact(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerCarry(), this);
        getServer().getPluginManager().registerEvents(new PlayerPhaseUpListener(), this);
        //getServer().getPluginManager().registerEvents(new FamilyListener(), this);

        //getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        //getServer().getPluginManager().registerEvents(new SurrogateListener(), this);
        //getServer().getPluginManager().registerEvents(new RoleListener(), MCG.getInstance());
    }

    private void loadServerData(){
        FileConfiguration config = getConfig();
        ConfigurationSection configSection = config.getConfigurationSection("data.server");
        int year = configSection.getInt("year");
        serverYear = year;
    }

    private void saveServerData(){
        FileConfiguration config = getConfig();
        config.set("data.server.year", serverYear);
        saveConfig();
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

    private void setupServer(){
        overworld.setSpawnLocation(council.councilLocation);
        getServer().dispatchCommand(Bukkit.getConsoleSender(), "veryspicy true");
        // disable dayloop
        // create teams etc
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
Todo: Council countdown beeping
Todo: Setup config file for ...
Todo: Show baby cooldown in info command
 */