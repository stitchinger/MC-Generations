package io.georgeous.mcgenerations;

import org.bukkit.block.Banner;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;


public class Family {

    private String name;
    private String color;
    private Banner banner;
    private Player leader;
    //private Map<String,String> colorCodes = new HashMap<>();
    private String[] colorCodes = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"};

    public Family(String name){
        this.name = name;

        for(String c : colorCodes){
            String prefix = "§";
            c = prefix + c + "";
        }

        this.color = getRandomColor();
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


}

