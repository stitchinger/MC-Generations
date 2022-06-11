package io.georgeous.mcgenerations.files;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        // Time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        reports = (ArrayList<String>) cfg.getStringList(reported.getName());

        reports.add(reason + " | " + reporter.getName() + " | " + date);
        cfg.set(reported.getName(), reports);
        save();

        // Send to OP
        TextComponent reporterText = getTpLink(reporter.getName());
        TextComponent reportedText = getTpLink(reported.getName());
        Notification.opBroadcast(reporterText, new TextComponent(" reported "), reportedText);

        Notification.successMsg(reporter, "You reported " + reportedName + " for " + reason);
    }

    private static TextComponent getTpLink(String playerIGN){
        TextComponent linkText = new TextComponent(playerIGN);
        linkText.setColor(ChatColor.BLUE);
        linkText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + playerIGN));
        linkText.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to tp").color(ChatColor.GRAY).italic(true).create())
        );
        return linkText;
    }

    public static void printReports(Player player){
        if(cfg.getKeys(false).isEmpty()){
            Notification.errorMsg(player, "There are currently no reports to list");
        }

        for (String key : cfg.getKeys(false)) {
            player.sendMessage(key);
            ArrayList<String> reports = (ArrayList<String>) cfg.getStringList(key);
            reports.forEach(report -> {
                player.sendMessage(" - " + report);
            });
        }
    }

    public static boolean deleteReport(String playerIGN){
        if(cfg.get(playerIGN) != null){
            cfg.set(playerIGN, null);
            save();
            return true;
        }
        return false;
    }

    public static ArrayList<String> getReportedPlayers(){
        ArrayList<String> reported = new ArrayList<>();
        reported.addAll(cfg.getKeys(false));

        return reported;
    }
}
