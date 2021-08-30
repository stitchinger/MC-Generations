package io.georgeous.mcgenerations.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DebugCommandCompleter implements TabCompleter {

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