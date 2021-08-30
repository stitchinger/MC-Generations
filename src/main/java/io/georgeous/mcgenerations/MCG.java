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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public final class MCG extends JavaPlugin {
    public static MCG plugin;
    public DataManager data;

    public static World overworld;
    public static Council council;
    public static long daySpeed = 2;

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

        SurroManager.enable();
        PlayerManager.enable();
        FamilyManager.enable();
        RoleManager.enable();

        makeBundleCraftable();

        overworld.setSpawnLocation(council.councilLocation);

        for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            //team.unregister();
        }

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
        System.out.println("MCG ? ========== [ MC Generations ] ==========");
        System.out.println("MCG ? Version: 0.1");
        System.out.println("MCG ? Plugin by: Georgeous.io");
        System.out.println("MCG ? ========== [ MC Generations ] ==========");
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
        getServer().getPluginCommand("gm").setExecutor(new GamemodeCommand());
        getServer().getPluginCommand("nick").setExecutor(new NickCommand());
        getServer().getPluginCommand("you").setExecutor(new YouAre());
        getServer().getPluginCommand("secinyear").setExecutor(new SecInYear());
        getServer().getPluginCommand("die").setExecutor(new DieCommand());
        getServer().getPluginCommand("dayspeed").setExecutor(new DaySpeedCommand());
        getServer().getPluginCommand("me").setExecutor(new MeCommand());
        getServer().getPluginCommand("debug").setExecutor(new DebugCommand());
        getCommand("debug").setTabCompleter(new DebugCommandCompleter());

        //Deactivate Vanilla commands
        getServer().getPluginCommand("msg").setExecutor(new CommandDeactivator());
        getServer().getPluginCommand("w").setExecutor(new CommandDeactivator());
        getServer().getPluginCommand("say").setExecutor(new CommandDeactivator());
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

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.discoverRecipe(key);
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