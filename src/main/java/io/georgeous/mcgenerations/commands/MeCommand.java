package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MeCommand implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender sender,  Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;

        if(RoleManager.get(player) == null){
            Notification.errorMsg(player, "No role found");
            return true;
        }
        PlayerRole role = RoleManager.get(player);

        Notification.neutralMsg(player, role.getName() + " " + role.getFamily().getColoredName());
        Notification.neutralMsg(player, "Age: " + role.am.getAge());

        return false;
    }


}
