package io.georgeous.mcgenerations.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete (CommandSender sender, Command cmd, String label, String[] args){
        List<String> l = new ArrayList<String>();
        if(cmd.getName().equalsIgnoreCase("debug") && args.length >= 0){
            if(sender instanceof Player){
                Player player = (Player) sender;

                List<String> list = new ArrayList<>();
                l.add("surrogate");
                l.add("pets");
                l.add("banner");
                l.add("banner");
                l.add("saveplayer");
                l.add("council");

                return l;

            }
        }
        if(cmd.getName().equalsIgnoreCase("pets") && args.length >= 0){
            if(sender instanceof Player){
                Player player = (Player) sender;

                List<String> list = new ArrayList<>();
                l.add("release");
                l.add("passon");


                return l;

            }
        }
        return l;
    }
}
