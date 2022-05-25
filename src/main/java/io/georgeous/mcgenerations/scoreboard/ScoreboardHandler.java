package io.georgeous.mcgenerations.scoreboard;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScoreboardHandler {

    private final String title;
    private final List<String> lines;
    private final SimpleDateFormat dateFormat;
    private ScoreboardRefreshTask refreshTask;


    public ScoreboardHandler() {

        dateFormat = new SimpleDateFormat(MCG.getInstance().getFileManager().getScoreboardFile().getString("dateformat"));
        title = ChatColor.translateAlternateColorCodes('&', MCG.getInstance().getFileManager().getScoreboardFile().getString("title"));
        lines =  MCG.getInstance().getFileManager().getScoreboardFile().getStringList("lines");
        for(int i = 0; i < lines.size(); i++) {
            lines.set(i, ChatColor.translateAlternateColorCodes('&', lines.get(i)));
        }
        refreshTask = new ScoreboardRefreshTask(this);
        refreshTask.runTaskTimer(MCG.getInstance(), 20, 20);

    }

    public void refreshPlayer(Player playerToRefresh) {

        boolean hasNoScoreboard = playerToRefresh.getScoreboard().getObjective("dummy_sidebar") == null;
        if(hasNoScoreboard){
            registerPlayer(playerToRefresh);
        }

        Objective objective = playerToRefresh.getScoreboard().getObjective("dummy_sidebar");
        final int maxScore = lines.size();
        String spaceCounter = "";
        for(int i = 0; i < lines.size(); i++) {
            Team team = playerToRefresh.getScoreboard().getTeam(String.valueOf(i));
            if(lines.get(i).equalsIgnoreCase("[space]")) {
                team.setPrefix(spaceCounter);
                spaceCounter += " ";
            } else {
                team.setPrefix(replacePlaceholders(lines.get(i), playerToRefresh));
            }
            objective.getScore(ChatColor.values()[i].toString()).setScore(maxScore-i);
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void registerPlayer(Player playerToRegister) {

        Scoreboard scoreboard = playerToRegister.getScoreboard();
        Objective objective = scoreboard.getObjective("dummy_sidebar");

        if(objective == null) {
            objective = scoreboard.registerNewObjective("dummy_sidebar", "bbb", replacePlaceholders(title, playerToRegister));
        }

        for(int i = 0; i < lines.size(); i++) {
            Team team = scoreboard.registerNewTeam(String.valueOf(i));
            team.addEntry(ChatColor.values()[i].toString());
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);


        // Nocollision team for Surrogate
        Team team = scoreboard.getTeam("nocollision");
        if(team == null){
            team = scoreboard.registerNewTeam("nocollision");
        }
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        // surrogate joins no collision team
        // this needs to be where the surrogate is created
        Villager villager = SurrogateManager.getInstance().getVillager(playerToRegister);
        if(villager != null) {
            team.addEntry(villager.getUniqueId().toString());
        }

    }




    private String replacePlaceholders(String toReplace, Player player) {
        PlayerWrapper playerWrapper = PlayerManager.getInstance().getWrapper(player);

        /*
        long playTime = playerWrapper.getPlayTime();

        String hours = ((playTime/3600 < 10) ? "0" : "")+ playTime/3600;
        playTime -= playTime/3600;
        String minutes = ((playTime/60 < 10) ? "0" : "")+ playTime/60;
        playTime-= playTime/60;
        String seconds = ((playTime < 10) ? "0" : "")+ playTime;

         */

        return toReplace
                //.replace("[playtime]", hours + ":" + minutes + ":" + seconds)
                //.replace("[timeofjoin]", dateFormat.format(new Date(playerWrapper.getTimeOfJoin())))
                .replace("[name]", (RoleManager.getInstance().get(player) == null)? "Not found" : String.valueOf(RoleManager.getInstance().get(player).getName()))
                .replace("[familyname]", (RoleManager.getInstance().get(player) == null)? "Not found" : String.valueOf(RoleManager.getInstance().get(player).getFamily().getName()))
                .replace("[age]", (RoleManager.getInstance().get(player) == null)? "Not found" : String.valueOf(RoleManager.getInstance().get(player).getAgeManager().getAge()))
                .replace("[generation]", (RoleManager.getInstance().get(player) == null)? "Not found" : String.valueOf(RoleManager.getInstance().get(player).getGeneration()))

                .replace("[year]", String.valueOf(MCG.serverYear))
                .replace("[lifes]", (PlayerManager.getInstance().getWrapper(player) == null)? "Not found" : String.valueOf(PlayerManager.getInstance().getWrapper(player).getLifes()));

    }

}
