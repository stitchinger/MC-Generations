package io.georgeous.mcgenerations.scoreboard;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.List;
import java.util.UUID;

public class ScoreboardHandler {

    private final String title;
    private final List<String> lines;
    private static ScoreboardHandler instance;

    public static ScoreboardHandler get(){
        if(instance == null){
            instance = new ScoreboardHandler();
        }
        return instance;
    }

    private ScoreboardHandler() {

        title = "1hour1life.minehut.gg";
        lines =  MCG.getInstance().getFileManager().getScoreboardFile().getStringList("lines");
        for(int i = 0; i < lines.size(); i++) {
            lines.set(i, ChatColor.translateAlternateColorCodes('&', lines.get(i)));
        }

        /*
        new BukkitRunnable(){
            @Override
            public void run() {
                for(UUID player : PlayerManager.get().getWrapperAttachedPlayers()) {
                    refreshScoreboardOfPlayer(Bukkit.getPlayer(player));
                }
            }
        }.runTaskTimer(MCG.getInstance(), 20 , 20);

         */

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

    public void refreshScoreboardOfPlayer(Player toRefresh) {

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


        if(toRefresh.getScoreboard().getObjective("dummy_sidebar") == null){
            registerPlayer(toRefresh);
        }
        Objective objective = toRefresh.getScoreboard().getObjective("dummy_sidebar");
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
                scoreboardTextLine.setPrefix(replacePlaceholders(lines.get(i), toRefresh));
            }
            objective.getScore(ChatColor.values()[i].toString()).setScore(maxScore-i);
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    }


    private String replacePlaceholders(String toReplace, Player player) {
        PlayerWrapper playerWrapper = PlayerManager.get().getWrapper(player);
        if(playerWrapper != null){
            toReplace = toReplace
                    .replace("[lifes]", String.valueOf(playerWrapper.getLifes()));
        }

        PlayerRole playerRole = RoleManager.get().get(player);
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
    }

}
