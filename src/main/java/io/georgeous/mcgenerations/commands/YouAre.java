package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.NameManager;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.piggyback.Piggyback;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class YouAre implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length != 2) {
            Notification.errorMsg(player, "Usage: /You are Lisa");
        }

        if (args.length == 2) {
            nameChild(player, args[1]);
        }
        return true;
    }

    public void nameChild(Player motherPlayer, String rawName) {
        PlayerRole motherPlayerRole = RoleManager.getInstance().get(motherPlayer);
        if (Piggyback.carryCoupleMap.get(motherPlayer) == null) {
            Notification.errorMsg(motherPlayer, "You need to hold your baby for naming it.");
            return;
        }

        Entity target = Piggyback.carryCoupleMap.get(motherPlayer).getTarget();
        String first = rawName.substring(0, 1).toUpperCase() + rawName.substring(1);

        if (target instanceof Player childPlayer) {
            PlayerRole childPlayerRole = RoleManager.getInstance().get(childPlayer);

            if (!motherPlayerRole.getMotherController().isOwnChild(childPlayerRole)) {
                // Disable renaming of stranger children?
                //Notification.errorMsg(motherPlayer, "You can only rename your own children!");
                //return;
            }


            if (childPlayerRole.isRenamed()) {
                Notification.errorMsg(motherPlayer, "You can name your child only once.");
                return;
            }

            if(NameManager.nameInUse(first)) {
                Notification.errorMsg(motherPlayer, "This name is already in use by a player. Please pick another.");
                return;
            }
            String oldName = childPlayerRole.getName();
            Piggyback.stopCarry(motherPlayer);
            childPlayerRole.setName(first);
            NameManager.registerName(first);
            NameManager.deregisterName(oldName);
            childPlayerRole.setRenamed(true);
            Notification.successMsg(motherPlayer, "You changed your childs name to " + first);
        } else {
            target.setCustomName(first + " " + motherPlayerRole.family.getColoredName());
            Notification.successMsg(motherPlayer, "You changed the mobs name to " + first);
        }


    }
}