package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BabyHandlerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player))
            return true;

        PlayerRole role = RoleManager.get().get(player);

        if (role == null) {
            Notification.errorMsg(player, "No role found");
            return true;
        }

        if(role.getPhaseManager().getCurrentPhase().getId() < 3){
            Notification.errorMsg(player, "You are too young for this");
            return true;
        }

        if(player.getInventory().firstEmpty() == -1) {
            Notification.errorMsg(player, "Inventory full!");
            return true;
        }

        Util.giveItemIfNotInInventory(ItemManager.createBabyHandler(), player.getInventory());

        return false;
    }
}