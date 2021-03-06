package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DieCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return true;

        if(player.getLocation().getWorld() == McgConfig.getCouncilLocation().getWorld()){
            double distanceToCouncil = McgConfig.getCouncilLocation().distance(player.getLocation());

            if(distanceToCouncil <= 50){
                Notification.errorMsg(player, "You cant use this command in the council!");
                return true;
            }
        }


        if (RoleManager.get().get(player) != null) {
            PlayerRole role = RoleManager.get().get(player);
            if(role.getAgeManager().getAge() < 1){
                Notification.errorMsg(player, "You are too young to use this command!");
                return true;
            }
        } else{
            Notification.errorMsg(player, "No role found");
        }

        player.setHealth(0);
        //player.damage(999);


        return false;
    }
}