package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NamePreferenceCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player player))
            return true;

        PlayerWrapper wrapper = PlayerManager.get().getWrapper(player);
        if(wrapper == null)
            return true;

        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /namepreference <male/female/random>");
            return true;
        }

        String arg = args[0].toLowerCase();

        switch (arg) {
            case "male":
                wrapper.setNamePreference("male");
                Notification.successMsg(player, "You set your name preference to male");
                break;
            case "female":
                wrapper.setNamePreference("female");
                Notification.successMsg(player, "You set your name preference to female");
                break;
            case "random":
                wrapper.setNamePreference("random");
                Notification.successMsg(player, "You set your name preference to random");
                break;
            default:
                Notification.errorMsg(player, "Invalid preference");
                return true;
        }


        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("namepreference")) {
            if (sender instanceof Player) {
                l.add("male");
                l.add("female");
                l.add("random");

                return l;
            }
        }
        return l;

    }
}
