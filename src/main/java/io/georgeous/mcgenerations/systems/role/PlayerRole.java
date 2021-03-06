package io.georgeous.mcgenerations.systems.role;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.role.components.PlayerAge;
import io.georgeous.mcgenerations.systems.role.components.PlayerMother;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.NameManager;
import io.georgeous.petmanager.PetManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.haoshoku.nick.api.NickAPI;
import xyz.haoshoku.nick.api.NickConfig;
import xyz.haoshoku.nick.api.NickScoreboard;

public class PlayerRole {
    // todo Kill role if player is offline for more than 5 min
    private Player player;
    // Managers
    private final PlayerAge playerAge;
    private final PhaseManager phaseManager;
    private final PlayerMother playerMother;
    public Family family;
    public int generation;
    private String mothersName = null;
    public boolean isDead = false;
    private boolean offline = false;
    private long lastSeenOnline = 0L;
    private String name;
    private boolean isRenamed = false;
    private boolean usedAdopt = false;
    private Inventory offlineInventory;
    private String skinColor;
    private String hairColor;

    public PlayerRole(Player player, String name, int age, int generation, Family family) {
        this.player = player;
        this.family = family;
        this.generation = generation;

        setName(name);

        playerAge = new PlayerAge(this, age);
        phaseManager = new PhaseManager(this, playerAge);
        playerMother = new PlayerMother(this);
    }

    public void update() {
        if (!isDead) {
            playerAge.update();
            phaseManager.update();
            playerMother.update();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nickPlayer(player, name);
        if (SurrogateManager.map.get(getPlayer()) != null) {
            SurrogateManager.getInstance().destroySurrogateOfPlayer(getPlayer());
            SurrogateManager.getInstance().createSurrogateForPlayer(getPlayer(), name + " " + family.getColoredName());
        }
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
        NameManager.deregisterName(this.name);
        NickAPI.resetNick(player);
        NickAPI.resetSkin(player);

        refreshHealthBar();
    }

    public void refreshNick(){
        nickPlayer(player, name);
        updateScoreboard();
        ScoreboardHandler.get().refreshScoreboardOfPlayer(player);
        phaseManager.start();
    }

    public void updateScoreboard() {
        NickScoreboard.write(name, player.getUniqueId().toString().substring(0, 15),  getFamily().getColoredName() + " ", "" , true, ChatColor.WHITE);
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

    public void setFamily(Family family) {
        this.family = family;
    }

    public void die() {
        if (!isDead) {

            isDead = true;

            if (player.getHealth() != 0) {
                player.setHealth(0);
            }
            NameManager.deregisterName(name);
            SurrogateManager.getInstance().destroySurrogateOfPlayer(player);
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

    public void setPlayer(Player player){
        this.player = player;
    }

    public void setRenamed(boolean toggle){
        isRenamed = toggle;
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

    public int getGeneration(){
        return this.generation;
    }

    public boolean getUsedAdopt(){
        return usedAdopt;
    }

    public void setUsedAdopt(boolean value){
        usedAdopt = value;
    }

    public void setIsOffline(boolean value){
        offline = value;
        if(offline){
            lastSeenOnline = System.currentTimeMillis();
        }
    }

    public boolean isOffline(){
        return offline;
    }

    public Long getLastSeenOnline(){
        return lastSeenOnline;
    }

    public String getMothersName(){
        return mothersName;
    }

    public void setMothersName(String name){
        mothersName = name;
    }

    public Inventory getOfflineInventory() {
        return offlineInventory;
    }

    public void setOfflineInventory(Inventory offlineInventory) {
        this.offlineInventory = offlineInventory;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }
}