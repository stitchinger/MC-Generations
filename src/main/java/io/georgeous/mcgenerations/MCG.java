package io.georgeous.mcgenerations;


import io.georgeous.mcgenerations.commands.CommandDeactivator;
import io.georgeous.mcgenerations.commands.admin.*;
import io.georgeous.mcgenerations.commands.admin.mute.MuteCommand;
import io.georgeous.mcgenerations.commands.player.*;
import io.georgeous.mcgenerations.files.DataManager;
import io.georgeous.mcgenerations.files.FileManager;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.files.Reporter;
import io.georgeous.mcgenerations.listeners.*;
import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.BadWordFilter;
import io.georgeous.mcgenerations.utils.NameManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;
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
        plugin = this;
        printLoadupText();
        //this.saveDefaultConfig();
        //getConfig().options().copyDefaults(true);

        /*
        if(!(new File(this.getDataFolder().getPath() + "/scoreboard.yml").exists())) {
            this.saveResource("scoreboard.yml", false);
        }
        if(!(new File(this.getDataFolder().getPath() + "/playerdata.yml").exists())) {
            this.saveResource("playerdata.yml", false);
        }

         */


        BadWordFilter.init();

        fileManager = new FileManager(this.getDataFolder().getPath());
        McgConfig.setup(this.getDataFolder().getPath());
        Reporter.setup(this.getDataFolder().getPath());

        overworld = Bukkit.getWorlds().get(0);
        council = new Council(overworld);
        overworld.setSpawnLocation(McgConfig.getCouncilLocation());
        getConfig().options().copyDefaults(false);


        this.data = new DataManager();

        PlayerManager.get().enable();
        FamilyManager.enable();
        RoleManager.get().enable();
        NameManager.loadConfig();

        registerEvents();
        registerCommands();

        overworld.setSpawnLocation(McgConfig.getCouncilLocation());

        // Start Update-Function
        new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimer(this, 0L, 1L);

        new BukkitRunnable() {
            @Override
            public void run() {
                serverYear++;
            }
        }.runTaskTimer(this, 0, 20L * 60);

        serverYear = getConfig().getInt("data.server.year");

        saveConfig();


        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule doFireTick false");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule announceAdvancements false");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule playersSleepingPercentage 0");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule doImmediateRespawn true");
            }
        }.runTaskLater(this, 20L * 2);

    }

    @Override
    public void onDisable() {
        SurrogateManager.getInstance().destroyAllSurrogates();
        PlayerManager.get().disable();
        RoleManager.get().disable();
        FamilyManager.disable();

        NameManager.saveConfig();

        FileConfiguration config = getConfig();
        config.set("data.server.year", serverYear);
        saveConfig();


        resetPlayerScoreboards();
    }

    private void resetPlayerScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }


    public void printLoadupText() {
        getLogger().log(Level.FINE, "MCG ? ========== [ MC Generations ] ==========");
        getLogger().log(Level.FINE, "MCG ? Version: 0.1");
        getLogger().log(Level.FINE, "MCG ? Plugin by: Georgeous.io");
        getLogger().log(Level.FINE, "MCG ? ========== [ MC Generations ] ==========");
    }

    private void update() {
        RoleManager.get().update();
        SurrogateManager.getInstance().update();
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Interact(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerCraftListener(), this);
        getServer().getPluginManager().registerEvents(new BabyHandlerListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerCarry(), this);
        getServer().getPluginManager().registerEvents(new PlayerPhaseUp(), this);
        getServer().getPluginManager().registerEvents(new CreatureSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new CustomItemsListener(), this);
    }

    public void registerCommands() {
        registerAdminCommands();
        registerPlayerCommands();
    }

    private void registerPlayerCommands() {
        registerCommand("me", new MeCommand());
        registerCommand("you", new YouAreCommand());
        registerCommand("role", new RoleCommand());
        registerCommand("family", new FamilyCommand());
        registerCommand("babyhandler", new BabyHandlerCommand());
        registerCommand("die", new DieCommand());
        registerCommand("howto", new HowtoCommand());
        registerCommand("discord", new DiscordCommand());
        registerCommand("ad", new AdCommand());
        registerCommand("report", new ReportCommand());
        registerCommand("rules", new RulesCommand());
        registerCommand("adopt", new AdoptCommand());
        registerCommand("shareign", new ShareIgnCommand());
        registerCommand("namepreference", new NamePreferenceCommand());
        registerCommand("familyreborn", new FamilyRebornCommand());
        registerCommand("renameitem", new RenameItemCommand());
        registerCommand("msg", new CommandDeactivator());
        registerCommand("minecraft:me", new CommandDeactivator());
        registerCommand("w", new CommandDeactivator());
        registerCommand("say", new CommandDeactivator());
    }

    private void registerAdminCommands() {
        registerCommand("banish", new BanishCommand());
        registerCommand("mute", new MuteCommand());
        registerCommand("invsee", new InvseeCommand());
        registerCommand("realname", new RealNameCommand());
        registerCommand("debug", new DebugCommand());
        registerCommand("broadcast", new BroadcastCommand());
        registerCommand("secinyear", new SecInYear());
        registerCommand("dayspeed", new DaySpeedCommand());
        registerCommand("gm", new GamemodeCommand());
        registerCommand("nick", new NickCommand());
    }

    private void registerCommand(String command, CommandExecutor executor) {
        Optional.ofNullable(getCommand(command)).ifPresent(c -> c.setExecutor(executor));
    }

    public FileManager getFileManager() {
        return fileManager;
    }

}
