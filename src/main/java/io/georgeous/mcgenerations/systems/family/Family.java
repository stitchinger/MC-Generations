package io.georgeous.mcgenerations.systems.family;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Family {

    private String name;
    private String color;
    private long established;

    private final String uuid;
    private final List<PlayerRole> members;
    private PlayerRole leader;
    private boolean namedByLeader;
    public boolean isDead = false;

    public Family(String name) {
        this(name, UUID.randomUUID().toString());
    }

    public Family(String name, String uuid) {
        this.name = name;
        this.color = Util.getRandomColor();
        this.uuid = uuid;
        this.established = System.currentTimeMillis();
        this.members = new ArrayList<>();
        namedByLeader = false;
    }

    public String getUuid() {
        return uuid;
    }

    // Name
    public String getName() {
        return name;
    }

    public String getColoredName() {
        return color + getName() + ChatColor.RESET;
    }

    private void setName(String name) {
        this.name = name;
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
    public void addMember(PlayerRole role) {
        if (members.size() == 0) {
            setLeader(role);
        }
        members.add(role);
    }

    public void removeMember(PlayerRole role) {
        members.remove(role);
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

    public static boolean inSameFamily(PlayerRole one, PlayerRole two) {
        return one.getFamily() == two.getFamily();
    }

    public static boolean inSameFamily(Player one, Player two) {
        PlayerRole roleOne = RoleManager.get(one);
        PlayerRole roleTwo = RoleManager.get(two);

        if (roleOne == null || roleTwo == null) {
            return false;
        }

        return inSameFamily(roleOne, roleTwo);
    }
}