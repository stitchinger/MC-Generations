package io.georgeous.mcgenerations.scoreboard;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScoreboardHandler {

    private final ScoreboardManager manager;
    private final String title;
    private final List<String> lines;
    private final SimpleDateFormat dateFormat;
    private ScoreboardRefreshTask refreshTask;


    public ScoreboardHandler() {
        manager = Bukkit.getScoreboardManager();
        dateFormat = new SimpleDateFormat(MCG.getInstance().getFileManager().getScoreboardFile().getString("dateformat"));
        title = ChatColor.translateAlternateColorCodes('&', MCG.getInstance().getFileManager().getScoreboardFile().getString("title"));
        lines =  MCG.getInstance().getFileManager().getScoreboardFile().getStringList("lines");
        for(int i = 0; i < lines.size(); i++) {
            lines.set(i, ChatColor.translateAlternateColorCodes('&', lines.get(i)));
        }
        refreshTask = new ScoreboardRefreshTask(this);
        refreshTask.runTaskTimer(MCG.getInstance(), 20, 20);

    }

    public void refreshPlayer(Player toRefresh) {

        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dummy", "", title);
        objective.setDisplayName(title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        final int maxScore = lines.size();
        String spaceCounter = "";
        for(int i = 0; i < lines.size(); i++) {

            if(lines.get(i).equalsIgnoreCase("[space]")) {
                objective.getScore(spaceCounter).setScore(maxScore-i);
                spaceCounter += " ";
                continue;
            }

            objective.getScore(replacePlaceholders(lines.get(i), toRefresh)).setScore(maxScore-i);
        }
        toRefresh.setScoreboard(scoreboard);
    }


    private String replacePlaceholders(String toReplace, Player player) {

        PlayerWrapper playerWrapper = PlayerManager.getInstance().getWrapper(player);
        Location lastLocation = playerWrapper.getLastBedLocation();

        long playTime = playerWrapper.getPlayTime();

        String hours = ((playTime/3600 < 10) ? "0" : "")+ playTime/3600;
        playTime -= playTime/3600;
        String minutes = ((playTime/60 < 10) ? "0" : "")+ playTime/60;
        playTime-= playTime/60;
        String seconds = ((playTime < 10) ? "0" : "")+ playTime;

        return toReplace
                .replace("[diedofage]", String.valueOf(playerWrapper.getDiedOfOldAge()))
                .replace("[karma]", String.valueOf(playerWrapper.getKarma()))
                .replace("[lastbedlocation]", (lastLocation == null)? "Not found":"X: " + lastLocation.getBlockX() + " Y: " + lastLocation.getBlockY() + " Z: " + lastLocation.getBlockZ())
                .replace("[lives]", String.valueOf(playerWrapper.getLives()))
                .replace("[name]", player.getName())
                .replace("[playtime]", hours + ":" + minutes + ":" + seconds)
                .replace("[timeofjoin]", dateFormat.format(new Date(playerWrapper.getTimeOfJoin())))
                .replace("[age]", String.valueOf(RoleManager.getInstance().get(player).getAgeManager().getAge()));
    }

}
