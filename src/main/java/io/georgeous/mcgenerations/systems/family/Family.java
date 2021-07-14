package io.georgeous.mcgenerations.systems.family;

import io.georgeous.mcgenerations.role.PlayerRole;
import io.georgeous.mcgenerations.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Family {

    private String name;
    private String color;
    private long established;

    private String uuid;
    private List<PlayerRole> members;
    private PlayerRole leader;
    private boolean namedByLeader;
    public boolean isDead = false;


    public Family(String name){
        this.name = name;
        this.color = Util.getRandomColor();
        this.uuid = UUID.randomUUID().toString();
        this.established = System.currentTimeMillis();
        this.members = new ArrayList<>();
        namedByLeader = false;
    }

    public Family(String name, String uuid){
        this(name);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    // Name
    public String getName(){
        return name;
    }

    public String getColoredName(){
        return color + getName();
    }

    private void setName(String name){
        this.name = name;
    }

    public void rename(String name){
        if(false){
            System.out.println("Family name already defined by Leader");
            return;
        }
        setName(name);
        namedByLeader = true;
    }

    public boolean isRenamed(){
        return namedByLeader;
    }

    // Color
    public String getColor(){
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }

    // Leader
    public PlayerRole getLeader() {
        return leader;
    }

    public void setLeader(PlayerRole leader) {
        leader.getPlayer().sendMessage("You are now leader of the family " + name);
        this.leader = leader;
    }

    // Members
    public void addMember(PlayerRole role){
        if(members.size() == 0){
            setLeader(role);
        }
        members.add(role);
    }

    public void removeMember(PlayerRole role){
        members.remove(role);
        if(members.size() == 0){
            isDead = true;
        }

    }

    public int memberCount(){
        return members.size();
    }

    // Other
    public long getEstablished() {
        return established;
    }

    public void setEstablished(long established) {
        this.established = established;
    }
}
