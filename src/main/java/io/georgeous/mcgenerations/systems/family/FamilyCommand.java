package io.georgeous.mcgenerations.systems.family;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FamilyCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender,  Command command,  String label,  String[] args) {

        if (!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;

        if(RoleManager.get(player) == null){
            return true;
        }
        PlayerRole role = RoleManager.get(player);

        if(role.getFamily() == null){
            return true;
        }
        Family family = role.getFamily();


        switch (args[0]){

            case "info":
                player.sendMessage("Family: " + family.getColoredName());
                player.sendMessage("Members: " + family.memberCount());
                if(family.getLeader() != null){
                    player.sendMessage("Leader: " + family.getLeader().getName());
                }

                break;
            case "rename":
                attemptFamilyRename(role,family,args[1]);
                break;
            case "list":
                player.sendMessage(familiesToString());
                break;
            default:
                Notification.errorMsg(player, "Command not found");
        }

        return false;
    }

    public void attemptFamilyRename(PlayerRole role, Family family, String name){
        if (!role.pm.getCurrentPhase().getName().equalsIgnoreCase("child")) {
            Notification.errorMsg(role.getPlayer(), "You are too old to rename your Family");
            return;
        }

        if (family.isRenamed()) {
            Notification.errorMsg(role.getPlayer(), "Families can be renamed only one");
            return;
        }

        String last = name.substring(0, 1).toUpperCase() + name.substring(1);
        if (last.length() <= 2) {
            Notification.errorMsg(role.getPlayer(), "Families name is too short" );
            return;
        }

        family.rename(last);
        Notification.successMsg(role.getPlayer(), "You changed your families name to " + family.getColoredName() );
        role.updateScoreboard();
    }

    private String familiesToString(){
        String msg = "";
        for(Family f : FamilyManager.getAll()){
            msg += (f.getColoredName() + ", ");
        }
        return msg;
    }

    @Override
    public List<String> onTabComplete (CommandSender sender, Command cmd, String label, String[] args){
        List<String> l = new ArrayList<String>();
        if(cmd.getName().equalsIgnoreCase("family") && args.length >= 0){
            if(sender instanceof Player){

                l.add("list");
                l.add("rename");
                l.add("info");

                return l;
            }
        }
        return l;
    }
}