package io.georgeous.mcgenerations.commands.admin;

import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        if (!player.isOp()) {
            Notification.onlyForOp(player);
            return true;
        }
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            player.setGameMode(GameMode.SURVIVAL);
            Notification.successMsg(player, "Gamemode changed to Survival");
        } else {
            player.setGameMode(GameMode.CREATIVE);
            Notification.successMsg(player, "Gamemode changed to Creative");
        }

        return true;
    }
}
