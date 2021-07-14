package io.georgeous.mcgenerations.systems.pets;

import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PetCommand implements CommandExecutor, TabCompleter {

    private final PetManager petManager;

    public PetCommand(PetManager petManager) {
        this.petManager = petManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }
        Player player = (Player) sender;

        switch (args[0]) {
            case "release":
                petManager.releaseAllPets(player);
                break;
            case "passon":
                Player otherPlayer = Util.findClosestPlayer(player, 5d);
                petManager.passPets(player, otherPlayer);
                break;
            case "count":
                player.sendMessage("Pets: " + petManager.getPetCount(player));
                break;
            case "save":
                petManager.save();
                break;
            case "load":
                petManager.restore(player);
                break;
            default:
                player.sendMessage("Command not found");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<String>();
        if(cmd.getName().equalsIgnoreCase("pets") && args.length >= 0){
            if(sender instanceof Player){
                Player player = (Player) sender;

                l.add("release");
                l.add("passon");
                l.add("count");
                l.add("save");
                l.add("load");
                return l;
            }
        }
        return l;
    }
}
