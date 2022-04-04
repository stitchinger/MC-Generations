package io.georgeous.mcgenerations;

import io.georgeous.mcgenerations.commands.*;
import io.georgeous.mcgenerations.files.DataManager;
import io.georgeous.mcgenerations.listeners.*;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.commands.DieCommand;
import io.georgeous.mcgenerations.systems.role.commands.SecInYear;
import io.georgeous.mcgenerations.systems.role.commands.YouAre;
import io.georgeous.mcgenerations.systems.role.lifephase.listeners.PlayerPhaseUp;
import io.georgeous.mcgenerations.systems.surrogate.SurroManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;

public final class MCG extends JavaPlugin {
    public static MCG plugin;
    public DataManager data;

    public static World overworld;
    public static Council council;
    public static long daySpeed = 2; // Default is 1

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

        //log(String.valueOf(getConfig().getInt("")));

        this.data = new DataManager();

        registerEvents();
        registerCommands();

        SurroManager.enable();
        PlayerManager.enable();
        FamilyManager.enable();
        RoleManager.enable();


        makeBundleCraftable();

        overworld.setSpawnLocation(council.councilLocation);

//        for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
//            //team.unregister();
//        }

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
        SurroManager.disable();
        PlayerManager.disable();
        RoleManager.disable();
        FamilyManager.disable();
    }

    public void printLoadupText() {
        getLogger().log(Level.FINE,"MCG ? ========== [ MC Generations ] ==========");
        getLogger().log(Level.FINE,"MCG ? Version: 0.1");
        getLogger().log(Level.FINE,"MCG ? Plugin by: Georgeous.io");
        getLogger().log(Level.FINE,"MCG ? ========== [ MC Generations ] ==========");
    }

    private void update() {
        RoleManager.update();
        SurroManager.update();

        overworld.setTime(overworld.getTime() + daySpeed);
        // one day 24000
        // 20 ticks = 1 sec
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Interact(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerCarry(), this);
        getServer().getPluginManager().registerEvents(new PlayerPhaseUp(), this);
    }

    public void registerCommands() {
        Objects.requireNonNull(getServer().getPluginCommand("gm")).setExecutor(new GamemodeCommand());
        Objects.requireNonNull(getServer().getPluginCommand("nick")).setExecutor(new NickCommand());
        Objects.requireNonNull(getServer().getPluginCommand("you")).setExecutor(new YouAre());
        Objects.requireNonNull(getServer().getPluginCommand("secinyear")).setExecutor(new SecInYear());
        Objects.requireNonNull(getServer().getPluginCommand("die")).setExecutor(new DieCommand());
        Objects.requireNonNull(getServer().getPluginCommand("dayspeed")).setExecutor(new DaySpeedCommand());
        Objects.requireNonNull(getServer().getPluginCommand("me")).setExecutor(new MeCommand());
        Objects.requireNonNull(getServer().getPluginCommand("debug")).setExecutor(new DebugCommand());
        Objects.requireNonNull(getCommand("debug")).setTabCompleter(new DebugCommandCompleter());

        //Deactivate Vanilla commands
        Objects.requireNonNull(getServer().getPluginCommand("msg")).setExecutor(new CommandDeactivator());
        Objects.requireNonNull(getServer().getPluginCommand("w")).setExecutor(new CommandDeactivator());
        Objects.requireNonNull(getServer().getPluginCommand("say")).setExecutor(new CommandDeactivator());
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

        /*
        for (Player player : Bukkit.getOnlinePlayers()) {

        }

         */
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