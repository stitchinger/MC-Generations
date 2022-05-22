package io.georgeous.mcgenerations.systems.role;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.role.components.PlayerAge;
import io.georgeous.mcgenerations.systems.role.components.PlayerMother;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.petmanager.PetManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.haoshoku.nick.api.NickAPI;
import xyz.haoshoku.nick.api.NickScoreboard;

public class PlayerRole {
    // todo Kill role if player is offline for more than 5 min
    private final Player player;
    // Managers
    private final PlayerAge playerAge;
    private final PhaseManager phaseManager;
    private final PlayerMother playerMother;
    public Family family;
    public int generation = 1;
    public boolean isDead = false;
    private String name;
    private boolean isRenamed = false;

    public PlayerRole(Player player, String name, int age, Family family) {
        this.player = player;
        this.family = family;

        setName(name);

        playerAge = new PlayerAge(this, age);
        phaseManager = new PhaseManager(this, playerAge);
        playerMother = new PlayerMother(this);

        this.family.addMember(this);
    }

    public void update() {
        if (!isDead) {
            playerMother.update();
            playerAge.update();
            phaseManager.update();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nickPlayer(player, name);
        if (SurrogateManager.map.get(getPlayer()) != null) {
            SurrogateManager.getInstance().destroyPlayer(getPlayer());
            SurrogateManager.getInstance().create(getPlayer(), name + " " + family.getColoredName());
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

    public void unnickPlayer(Player player) {
        NickAPI.resetNick(player);
        NickAPI.resetSkin(player);

        refreshHealthBar();
    }

    public void updateScoreboard() {
        NickScoreboard.write(name, player.getUniqueId().toString().substring(0, 15), "", " " + getFamily().getColoredName(), false, ChatColor.WHITE);
        NickScoreboard.updateScoreboard(name);
    }

    public void refreshHealthBar() {
        player.setFoodLevel(player.getFoodLevel() + 2);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setFoodLevel(player.getFoodLevel() - 2);
            }
        }.runTaskLater(MCG.getInstance(), 10);
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
            }.runTaskLater(MCG.getInstance(), 20);

            if (player.getHealth() != 0) {
                player.setHealth(0);
            }
        }
    }

    public void passOnPetsToDescendent() {
        if (playerMother.getOldestChild() != null) {
            PetManager.passPets(this.getPlayer(), playerMother.getOldestChild().getPlayer());

        } else {
            PetManager.releaseAllPets(this.getPlayer());
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void babyFeedEffect() {
        Location location = player.getLocation();
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.COMPOSTER, location, 40, 0.5, 0.5, 0.5);
            world.playSound(location, Sound.ENTITY_GENERIC_DRINK, 1, 1);
        }
    }

    public boolean compare(PlayerRole role) {
        return this == role;
    }

    public PlayerAge getAgeManager() {
        return playerAge;
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public PlayerMother getMotherController() {
        return playerMother;
    }
}