package io.georgeous.mcgenerations.gadgets;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> l = new ArrayList<String>();

        if (cmd.getName().equalsIgnoreCase("pets") && args.length >= 0) {
            List<String> list = new ArrayList<>();
            l.add("release");
            l.add("passon");
            l.add("count");
            return l;
        }

        return l;
    }
}
