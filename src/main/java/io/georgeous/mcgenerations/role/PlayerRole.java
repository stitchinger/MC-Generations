package io.georgeous.mcgenerations.role;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.role.components.AgeManager;
import io.georgeous.mcgenerations.role.components.MotherController;
import io.georgeous.mcgenerations.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.systems.surrogate.SurroManager;
import org.bukkit.ChatColor;
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
    public Family family;

    public int generation = 1;
    private boolean  isRenamed = false;
    public boolean isDead = false;

    // Managers
    public AgeManager am;
    public PhaseManager pm;
    public MotherController mc;

    public PlayerRole(Player player, String name, int age, Family family) {
        this.player = player;
        this.family = family;

        setName(name);

        am = new AgeManager(this, age);
        pm = new PhaseManager(this, am);
        mc = new MotherController(this);

        this.family.addMember(this);
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
            SurroManager.destroy(getPlayer());
            SurroManager.create(getPlayer(), name + " " + family.getColoredName());
        }
    }

    public void rename(String name) {
        if (isRenamed) {
            System.out.println("Role already named by mother");
            return;
        }
        setName(name);
        isRenamed = true;
    }

    public boolean isRenamed() {
        return isRenamed;
    }

    public void nickPlayer(Player player, String name) {
        NickAPI.nick(player, name);
        NickAPI.refreshPlayer(player);

        updateScoreboard();
        refreshHealthBar();
    }

    public void unnickPlayer(Player player){
        NickAPI.resetNick(player);
        NickAPI.resetSkin(player);
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
        }.runTaskLater(MCG.getInstance(),10);
    }

    public Family getFamily() {
        return family;
    }

    public void die() {
        if (!isDead) {
            isDead = true;
            passOnPetsToDescendent();
            new BukkitRunnable() {
                @Override
                public void run() {
                    unnickPlayer(player);
                }
            }.runTaskLater(MCG.getInstance(),20);

            if (player.getHealth() != 0) {
                player.setHealth(0);
            }
        }
    }

    public void passOnPetsToDescendent() {
        if (mc.getOldestChild() != null) {
            MCG.getInstance().petManager.passPets(this.getPlayer(), mc.getOldestChild().getPlayer());
        } else {
            MCG.getInstance().petManager.releaseAllPets(this.getPlayer());
        }
    }

    public Player getPlayer() {
        return player;
    }
}