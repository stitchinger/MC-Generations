package io.georgeous.mcgenerations.scoreboard;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreboardHandler {

    private final String title;

    private static ScoreboardHandler instance;

    public static ScoreboardHandler get(){
        if(instance == null){
            instance = new ScoreboardHandler();
        }
        return instance;
    }

    private ScoreboardHandler() {

        title = "1hour1life.minehut.gg";
        //lines =  MCG.getInstance().getFileManager().getScoreboardFile().getStringList("lines");


    }

    public void registerPlayer(Player toRegister) {
        Logger.log("Register Scoreboard: " + toRegister.getName());

        Scoreboard scoreboard = toRegister.getScoreboard();

        Objective objective = scoreboard.getObjective("dummy_sidebar");
        if(objective == null) {
            objective = scoreboard.registerNewObjective("dummy_sidebar", "bbb", "Charakter Name");
        }

        for(int i = 0; i < 10; i++) {
            Team team = scoreboard.registerNewTeam(String.valueOf(i));
            team.addEntry(ChatColor.values()[i].toString());
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void refreshScoreboardOfPlayer(Player toRefresh) {
        Logger.log("Refresh Scoreboard: " + toRefresh.getName());
        if(toRefresh.getScoreboard().getObjective("dummy_sidebar") == null){
            registerPlayer(toRefresh);
        }
        Objective objective = toRefresh.getScoreboard().getObjective("dummy_sidebar");
        List<String> lines = new ArrayList<>();

        PlayerRole role = RoleManager.get().get(toRefresh);
        if(role != null){
            Family family = role.getFamily();

            if(family != null){
                objective.setDisplayName(role.getName() + " " + role.getFamily().getColoredName());
            } else{
                objective.setDisplayName(role.getName());
            }


            lines.add(sbLine("Age", role.getAgeManager().getAge()));
            lines.add(sbLine("Gen", role.getGeneration()));
            lines.add(sbLine("Mother", role.getMothersName()));
            lines.add("[space]");


            if(family != null){
                lines.add(sbLine("Family", family.getColoredName()));
                lines.add(sbLine("- Est", family.getEstablished()));
                lines.add(sbLine("- Gens", family.getMaxGenerations()));
                lines.add(sbLine("- Members", family.getMembers().size()));
                lines.add("[space]");
            }
        }

        lines.add(sbLine("Year", MCG.serverYear));

        final int maxScore = lines.size();
        String spaceCounter = "";

        for(int i = 0; i < lines.size(); i++) {

            Team scoreboardTextLine = toRefresh.getScoreboard().getTeam(String.valueOf(i));
            if(scoreboardTextLine == null){
                return;
            }

            if(lines.get(i).equalsIgnoreCase("[space]")) {
                scoreboardTextLine.setPrefix(spaceCounter);
                spaceCounter += " ";
            } else {
                scoreboardTextLine.setPrefix(lines.get(i));
            }
            objective.getScore(ChatColor.values()[i].toString()).setScore(maxScore-i);
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    }

    private String sbLine(String label, Object value){
        return "§7" + label + ": §6" + String.valueOf(value);
    }


}
