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
            return true;
        }

        String arg = args[0].toLowerCase();

        switch (arg) {

            case "male":
                wrapper.setNamePreference("male");
                break;
            case "female":
                wrapper.setNamePreference("female");
            case "random":
                wrapper.setNamePreference("random");
            default:
                Notification.errorMsg(player, "Command not found");
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
