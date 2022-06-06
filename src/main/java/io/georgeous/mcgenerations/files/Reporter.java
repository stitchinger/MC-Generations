package io.georgeous.mcgenerations.files;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Reporter {

    private static File file;
    private static FileConfiguration cfg;

    public static void setup(String path){
        file = new File(path,"reports.yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cfg = YamlConfiguration.loadConfiguration(file);
        save();
    }

    public static FileConfiguration get(){
        return cfg;
    }

    public static void save(){
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload(){
        cfg = YamlConfiguration.loadConfiguration(file);
    }


    public static void reportPlayer(Player reporter, String reportedName, String reason){

        Player reported = NickAPI.getPlayerOfNickedName(reportedName);

        if(reporter == null || reported == null){
            Notification.errorMsg(reporter, "There is no player with that name online");
            return;
        }

        if(reporter == reported){
            Notification.errorMsg(reporter, "You cant report yourself");
            return;
        }

        ArrayList<String> reports = new ArrayList<>();

        if(cfg.get(reported.getName()) == null){
            cfg.set(reported.getName(), reports);
            save();
        }

        reports = (ArrayList<String>) cfg.getStringList(reported.getName());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        reports.add(reason + " | " + reporter.getName() + " | " + dtf.format(now));
        cfg.set(reported.getName(), reports);
        save();
        Notification.successMsg(reporter, "You reported " + reportedName + " for " + reason);
        //Notification.errorMsg(reported, "You have been reported for '" + reason + "'. Multiple reports will lead to a automatic ban");

        TextComponent reporterText = new TextComponent(reporter.getName());
        reporterText.setColor(ChatColor.BLUE);
        reporterText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + reporter.getName()));
        reporterText.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to tp").color(ChatColor.GRAY).italic(true).create())
        );

        TextComponent reportedText = new TextComponent(reported.getName());
        reportedText.setColor(ChatColor.BLUE);
        reportedText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + reported.getName()));
        reportedText.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to tp").color(ChatColor.GRAY).italic(true).create())
        );
        Notification.opBroadcast(reporterText, new TextComponent(" reported "), reportedText);
        //Notification.opBroadcast(reporter.getName() + " reported " + reported.getName());
    }

    public static void printReports(Player player){
        for (String key : cfg.getKeys(false)) {
            player.sendMessage(key);
            ArrayList<String> reports = (ArrayList<String>) cfg.getStringList(key);
            reports.forEach(report -> {
                player.sendMessage(" - " + report);
            });
        }
    }
}
