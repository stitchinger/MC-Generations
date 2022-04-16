package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DebugCommand implements CommandExecutor, TabCompleter {

    public DebugCommand() {
        CommandUtils.addTabComplete("debug", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

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

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("debug")) {
            if (sender instanceof Player) {
                l.add("council");
                return l;
            }
        }
        return l;
    }
}