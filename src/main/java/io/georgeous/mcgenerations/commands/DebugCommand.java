package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (!player.isOp()) {
            Notification.onlyForOp(player);
            return true;
        }

        if (args.length == 0) {
            PlayerWrapper wrapper = PlayerManager.get(player);
            if(wrapper == null){
                return true;
            }
            wrapper.setDebugMode(!(wrapper.isDebugMode()));
            Notification.neutralMsg(player,"Debug Mode: " + wrapper.isDebugMode());
            return true;
        }

        if ("council".equals(args[0])) {
            player.teleport(MCG.council.councilLocation);
        }
        return false;
    }
}