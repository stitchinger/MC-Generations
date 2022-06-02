package io.georgeous.mcgenerations.scoreboard;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateEntity;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
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

    public void registerPlayer(Player toRegister) {
        Scoreboard scoreboard = toRegister.getScoreboard();
        Objective objective = scoreboard.getObjective("dummy_sidebar");
        if(objective == null) objective = scoreboard.registerNewObjective("dummy_sidebar", "bbb", replacePlaceholders(title, toRegister));

        for(int i = 0; i < lines.size(); i++) {
            Team team = scoreboard.registerNewTeam(String.valueOf(i));
            team.addEntry(ChatColor.values()[i].toString());
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void refreshPlayer(Player toRefresh) {

        /*
        SURROGATE MANAGEMENT
         */
        /*
        Team team = toRefresh.getScoreboard().getTeam("nocollision");
        if(team == null){
            team = toRefresh.getScoreboard().registerNewTeam("nocollision");
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        SurrogateEntity surrogateEntity = SurrogateManager.getInstance().getVillager(toRefresh);
        if(surrogateEntity != null) {
            if(!team.getEntries().contains(surrogateEntity.getUniqueId().toString())) {
                team.addEntry(surrogateEntity.getUniqueId().toString());
            }
        }

         */

        /*
        if(toRefresh.getScoreboard().getObjective("dummy_sidebar") == null)registerPlayer(toRefresh);
        Objective objective = toRefresh.getScoreboard().getObjective("dummy_sidebar");
        final int maxScore = lines.size();
        String spaceCounter = "";
        for(int i = 0; i < lines.size(); i++) {

            team = toRefresh.getScoreboard().getTeam(String.valueOf(i));
            if(lines.get(i).equalsIgnoreCase("[space]")) {
                team.setPrefix(spaceCounter);
                spaceCounter += " ";
            } else {
                team.setPrefix(replacePlaceholders(lines.get(i), toRefresh));
            }
            objective.getScore(ChatColor.values()[i].toString()).setScore(maxScore-i);
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        */

    }


    private String replacePlaceholders(String toReplace, Player player) {
        PlayerWrapper playerWrapper = PlayerManager.getInstance().getWrapper(player);
        if(playerWrapper != null){
            toReplace = toReplace
                    .replace("[lifes]", String.valueOf(playerWrapper.getLifes()));
        }

        PlayerRole playerRole = RoleManager.getInstance().get(player);
        Family family = null;
        if(playerRole != null){
            family = playerRole.getFamily();
            toReplace = toReplace
                    .replace("[rolename]",String.valueOf( playerRole.getName()))
                    .replace("[age]", String.valueOf( playerRole.getAgeManager().getAge()))
                    .replace("[generation]", String.valueOf( playerRole.getGeneration()));
        }

        if(family != null){
            toReplace = toReplace
                    .replace("[familyname]", family.getColoredName())
                    .replace("[familyestablished]", String.valueOf(family.getEstablished()))
                    .replace("[familymembercount]", String.valueOf(family.memberCount()))
                    .replace("[familyleadername]", String.valueOf(family.getLeader().getName()))
                    .replace("[familygenerations]", String.valueOf(family.getMaxGenerations()));

        }

        toReplace = toReplace.replace("[year]", String.valueOf(MCG.serverYear));



        return toReplace;


        //.replace("[diedofage]", String.valueOf(playerWrapper.getDiedOfOldAge()))
                //.replace("[karma]", String.valueOf(playerWrapper.getKarma()))
                //.replace("[lastbedlocation]", (lastLocation == null)? "Not found":"X: " + lastLocation.getBlockX() + " Y: " + lastLocation.getBlockY() + " Z: " + lastLocation.getBlockZ())
                //.replace("[playtime]", hours + ":" + minutes + ":" + seconds)
                //.replace("[timeofjoin]", dateFormat.format(new Date(playerWrapper.getTimeOfJoin())))
    }

}
