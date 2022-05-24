package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.NameGenerator;
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
            PlayerRole playerRole = RoleManager.getInstance().get(player);
            nameChild(player, playerRole, args[1]);
        }
        return true;
    }

    public void nameChild(Player nameGiver, PlayerRole playerRole, String rawName) {
        if (Piggyback.carryCoupleMap.get(nameGiver) == null) {
            Notification.errorMsg(nameGiver, "You need to hold your baby for naming it.");
            return;
        }

        Entity target = Piggyback.carryCoupleMap.get(nameGiver).getTarget();
        String first = rawName.substring(0, 1).toUpperCase() + rawName.substring(1);

        if(NameGenerator.nameInUse(first)){
            Notification.errorMsg(nameGiver, "This name is already in use by a player. Please pick another.");
        }

        Piggyback.stopCarry(nameGiver);
        if (target instanceof Player) {
            PlayerRole targetsPlayerRole = RoleManager.getInstance().get((Player) target);

            if (!targetsPlayerRole.isRenamed()) {
                targetsPlayerRole.setName(first);
            } else {
                Notification.errorMsg(nameGiver, "You can name your child only once");
            }
        } else {
            target.setCustomName(first + " " + playerRole.family.getColoredName());
        }
        Notification.successMsg(nameGiver, "You changed your childs name to " + first);
    }
}