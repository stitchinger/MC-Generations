package io.georgeous.mcgenerations.family;

import org.bukkit.block.Banner;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;


public class Family {

    private String name;
    private String color;
    private long established;
    private Banner banner;
    private Player leader;
    //private Map<String,String> colorCodes = new HashMap<>();
    private String[] colorCodes = {"§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"};
    private String uuid;


    public Family(String name){
        this.name = name;
        this.color = getRandomColor();
        this.uuid = UUID.randomUUID().toString();
        this.established = System.currentTimeMillis();
    }

    public String getName(){
        return color + name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getColor(){
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }

    private String getRandomColor(){
        int i = (int)(Math.random() * colorCodes.length);
        return colorCodes[i];
    }

    public void getLeader(){

    }

    public void setLeader(){

    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getEstablished() {
        return established;
    }
}

