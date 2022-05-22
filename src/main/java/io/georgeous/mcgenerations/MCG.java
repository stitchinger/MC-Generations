package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.commands.*;
import io.georgeous.mcgenerations.files.DataManager;
import io.georgeous.mcgenerations.listeners.*;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public final class MCG extends JavaPlugin {
    public static MCG plugin;
    public static World overworld;
    public static Council council;
    public static long daySpeed = 2; // Default is 1
    public DataManager data;

    public static MCG getInstance() {
        return plugin;
    }


    @Override
    public void onEnable() {
        printLoadupText();
        plugin = this;
        overworld = Bukkit.getWorld("world");
        council = new Council(overworld);
        this.saveDefaultConfig();

        this.data = new DataManager();

        registerEvents();
        registerCommands();

        PlayerManager.getInstance();
        FamilyManager.enable();
        RoleManager.getInstance();

        makeBundleCraftable();

        overworld.setSpawnLocation(council.councilLocation);

        getServer().dispatchCommand(Bukkit.getConsoleSender(), "veryspicy true");


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
        SurrogateManager.getInstance().destroy();
        PlayerManager.getInstance().destroy();
        RoleManager.getInstance().destroy();
        FamilyManager.disable();
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

        overworld.setTime(overworld.getTime() + daySpeed);
        // one day 24000
        // 20 ticks = 1 sec
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Interact(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerCarry(), this);
        getServer().getPluginManager().registerEvents(new PlayerPhaseUp(), this);
        getServer().getPluginManager().registerEvents(new FamilyListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new SurrogateListener(), this);
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
}

/*
Todo: Council countdown beeping
TODO: Command for more Family info (Members, etc)
Todo: Carried clips in ground/walls
Todo: Setup config file for ...
Todo: Show baby cooldown in info command
Todo: Graves Plugin
 */