package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        PlayerRole role = RoleManager.get(player);

        if(role == null){
            Notification.errorMsg(player, "No role found");
            return true;
        }

        Notification.neutralMsg(player, role.getName() + " " + role.getFamily().getColoredName());
        Notification.neutralMsg(player, "Age: " + role.am.getAge());
        Notification.neutralMsg(player, "Phase: " + role.pm.getCurrentPhase().getName());

        return false;
    }
}