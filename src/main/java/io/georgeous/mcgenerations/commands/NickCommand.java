package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.role.PlayerRole;
import io.georgeous.mcgenerations.role.RoleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import xyz.haoshoku.nick.api.NickAPI;
import xyz.haoshoku.nick.api.NickScoreboard;

import java.util.Collection;

public class NickCommand implements CommandExecutor {

    public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {

        if ( ! ( sender instanceof Player) )
            return true;

        Player player = (Player) sender;
        if(!player.isOp()){
            sender.sendMessage("This command is only for OPs");
            return true;
        }

        if ( args.length == 0 ) {
            player.sendMessage( ChatColor.YELLOW + "/nick reset" );
            player.sendMessage( ChatColor.YELLOW + "/nick <Name>" );
            return true;
        }

        switch ( args[0].toLowerCase() ) {
            case "reset":
                NickAPI.resetNick( player );
                NickAPI.resetSkin( player );
                NickAPI.resetUniqueId( player );
                NickAPI.resetGameProfileName( player );
                NickAPI.refreshPlayer( player );
                player.sendMessage( ChatColor.DARK_RED + "Successfully reset nick" );
                break;

            default:
                String name = args[0].substring(0, 1).toUpperCase() + args[0].substring(1);
                nickPlayer(player, name);
                NickAPI.setSkin(player, name);

                break;
        }
        return true;
    }

    public void nickPlayer(Player player, String name){
        NickAPI.nick( player, name );
        NickAPI.refreshPlayer( player );

        if(RoleManager.get(player) != null){
            PlayerRole playerRole = RoleManager.get(player);
            NickScoreboard.write(name,"admin","", " " + playerRole.getFamily().getColoredName(), false, ChatColor.WHITE);
            NickScoreboard.updateScoreboard(name);
        }
        player.sendMessage( ChatColor.DARK_GREEN + "Successfully set the nickname to " + ChatColor.YELLOW + name );
    }

    private void updateNickNamesToScoreboard( Player player ) {
        if ( player == null )
            throw new NullPointerException( "Player cannot be null" );

        Scoreboard scoreboard;
        // Change it, if you are using main scoreboard
        if ( player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard() ) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard( scoreboard );
        } else
            scoreboard = player.getScoreboard();

        if ( scoreboard.getTeam( "nickedTeam" ) != null ) scoreboard.getTeam( "nickedTeam" ).unregister();

        Team team = scoreboard.registerNewTeam( "nickedTeam" );
        team.setPrefix( "MyPrefix " );
        team.setSuffix( "MySuffix " );

        Collection<String> values = NickAPI.getNickedPlayers().values();
        for ( String name : values )
            scoreboard.getTeam( "nickedTeam" ).addEntry( name );
    }
}
