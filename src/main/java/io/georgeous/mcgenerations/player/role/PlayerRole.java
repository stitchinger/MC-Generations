package io.georgeous.mcgenerations.player.role;

import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.family.Family;
import io.georgeous.mcgenerations.gadgets.PetManager;
import io.georgeous.mcgenerations.player.role.components.AgeManager;
import io.georgeous.mcgenerations.player.role.components.MotherController;
import io.georgeous.mcgenerations.player.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.manager.SurroManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.haoshoku.nick.api.NickAPI;
import xyz.haoshoku.nick.api.NickScoreboard;

public class PlayerRole {
    // todo Ablaufdatum
    // Wenn player länger als 5min offline, töte Role
    // Player
    private final Player player;
    private String name;
    private boolean namedByMother;
    public boolean isDead = false;

    // Family
    public Family family;
    public int generation;

    // Managers
    public AgeManager am;
    public PhaseManager pm;
    public MotherController mc;

    public PlayerRole(Player player, String name, int age, Family family) {
        this.player = player;
        this.generation = 1;
        this.family = family;
        setName(name);

        am = new AgeManager(this, age);
        pm = new PhaseManager(this, am);
        mc = new MotherController(this);
    }

    public void update() {
        if (!isDead) {
            mc.update();
            am.update();
            pm.update();
            if(player.getSaturation() > 0){
                player.setSaturation(0);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        nickPlayer(player,name);
        if (SurroManager.map.get(getPlayer()) != null) {
            SurroManager.destroySurrogate(getPlayer());
            SurroManager.create(getPlayer(), name + " " + family.getColoredName());
        }
    }

    public void nickPlayer(Player player, String name) {
        NickAPI.nick(player, name);
        NickAPI.refreshPlayer(player);

        updateScoreboard();
        refreshHealthBar();
    }

    public void updateScoreboard(){
        NickScoreboard.write(this.name, this.player.getUniqueId().toString().substring(0,15), "", " " + getFamily().getColoredName(), false, ChatColor.WHITE);
        NickScoreboard.updateScoreboard(name);
    }

    public void refreshHealthBar(){
        player.setFoodLevel(player.getFoodLevel() + 2);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setFoodLevel(player.getFoodLevel() - 2);
            }
        }.runTaskLater(Main.getPlugin(),10);
    }

    public void rename(String name) {
        if (namedByMother) {
            System.out.println("Role already named by mother");
            return;
        }
        setName(name);
        namedByMother = true;
    }

    public boolean isNamedByMother() {
        return namedByMother;
    }

    public Family getFamily() {
        return family;
    }

    public void die() {
        if (!isDead) {
            isDead = true;
            passOnPetsToDescendent();
            if (player.getHealth() != 0) {
                player.setHealth(0);
            }
        }
    }

    public void restoreFrom(ConfigurationSection config) {
        am.setAge(config.getInt("age"));
        name = config.getString("name");
        family.setName(config.getString("familyname"));
    }

    public void passOnPetsToDescendent() {
        if (mc.getOldestChild() != null) {
            PetManager.passPets(this.getPlayer(), mc.getOldestChild().getPlayer());
        } else {
            PetManager.releaseAllPets(this.getPlayer());
        }
    }

    public Player getPlayer() {
        return player;
    }
}
