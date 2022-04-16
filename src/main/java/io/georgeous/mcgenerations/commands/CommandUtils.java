package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class CommandUtils {

    protected static void addTabComplete(String command, TabCompleter completer) {
        PluginCommand pluginCommand = MCG.getInstance().getCommand(command);
        if (pluginCommand != null)
            pluginCommand.setTabCompleter(completer);
    }
}
