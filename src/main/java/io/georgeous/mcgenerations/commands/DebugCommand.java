package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.ServerConfig;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.utils.BlockFacing;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
            PlayerWrapper wrapper = PlayerManager.getInstance().getWrapper(player);
            if (wrapper == null) {
                return true;
            }
            wrapper.setDebugMode(!(wrapper.isDebugMode()));
            Notification.neutralMsg(player, "Debug Mode: " + wrapper.isDebugMode());
            return true;
        }

        if ("council".equals(args[0])) {
            player.teleport(ServerConfig.getInstance().getCouncilLocation());
            return true;
        }

        if ("health".equals(args[0])) {
            player.sendMessage("max: " + String.valueOf(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
            player.sendMessage("base: " + String.valueOf(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
            player.sendMessage("default: " + String.valueOf(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue()));
            player.sendMessage(String.valueOf(player.getHealth()));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();
        if (sender instanceof Player) {
            l.add("council");
        }
        return l;
    }
}