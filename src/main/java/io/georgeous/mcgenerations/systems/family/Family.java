package io.georgeous.mcgenerations.systems.family;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Logger;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Family {

    private final String uuid;
    private String name;
    private String color;
    private long established;
    private boolean namedByLeader;
    public boolean isDead = false;
    private int maxGenerations;
    private final List<PlayerRole> members;
    private PlayerRole leader;

    public Family(String name) {
        this(name, UUID.randomUUID().toString());
    }

    public Family(String name, String uuid) {
        this.name = name;
        this.color = Util.getRandomColor();
        this.uuid = uuid;
        this.established = MCG.serverYear;
        this.members = new ArrayList<>();
        namedByLeader = false;
        this.maxGenerations = 1;
    }

    public static boolean inSameFamily(PlayerRole one, PlayerRole two) {
        return !one.compare(two) && one.getFamily().compare(two.getFamily());
    }

    public static boolean inSameFamily(Player one, Player two) {
        PlayerRole roleOne = RoleManager.get().get(one);
        PlayerRole roleTwo = RoleManager.get().get(two);

        if (roleOne == null || roleTwo == null) {
            return false;
        }

        return inSameFamily(roleOne, roleTwo);
    }

    public String getUuid() {
        return uuid;
    }

    // Name
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getColoredName() {
        return color + getName() + ChatColor.RESET;
    }

    public void rename(String name) {
        setName(name);
        namedByLeader = true;
    }

    public boolean isRenamed() {
        return namedByLeader;
    }

    // Color
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Leader
    public PlayerRole getLeader() {
        return leader;
    }

    public void setLeader(PlayerRole leader) {
        Notification.neutralMsg(leader.getPlayer(), "You are now leader of the family " + this.color + "" + name);
        this.leader = leader;
    }

    // Members
    public List<PlayerRole> getMembers() {
        return this.members;
    }

    public void addMember(PlayerRole role) {
        if (members.size() == 0) {
            setLeader(role);
        }
        Logger.log(role.getName() + " added to family " + this.getName());
        role.setFamily(this);
        ScoreboardHandler.get().refreshScoreboardOfPlayer(role.getPlayer());

        if(!members.contains(role))
            members.add(role);
    }

    public void removeMember(PlayerRole role) {
        if(!members.remove(role)){
            Logger.log("Role not found in family");
            return;
        }
        Logger.log(role.getName() + " removed from family " + this.getName());
        role.setFamily(null);
        ScoreboardHandler.get().refreshScoreboardOfPlayer(role.getPlayer());

        if (members.size() == 0) {
            isDead = true;
        }
    }

    public int memberCount() {
        return members.size();
    }

    // Other
    public long getEstablished() {
        return established;
    }

    public void setEstablished(long established) {
        this.established = established;
    }

    public boolean compare(Family family) {
        return this == family;
    }

    public void setMaxGenerations(int gen){
        maxGenerations = Math.max(gen, maxGenerations);
        Top10.get().update(this);
    }

    public int getMaxGenerations(){
        return maxGenerations;
    }
}