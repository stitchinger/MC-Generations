package io.georgeous.mcgenerations.commands;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class HowtoCommand implements CommandExecutor, TabCompleter {

    private static final String headingStyle = "§l§u§9";




    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;


        if (args.length == 0) {
            // Summary
            player.sendMessage("");
            player.sendMessage(headingStyle + "-- HOW TO --§r");
            player.sendMessage("§f ► Hearts");
            player.sendMessage("§f ► Aging");
            player.sendMessage("§f ► Spawning");
            player.sendMessage("§f ► Baby");
            player.sendMessage("§f ► Mother");
            player.sendMessage("§f ► Family");
            player.sendMessage("§6e.g. Use command '/howto baby' for details");
            player.sendMessage("");
            return true;
        }

        String arg = args[0].toLowerCase();

        switch (arg) {

            case "hearts":
                player.sendMessage("");

                printHeading(player, "HEARTS");
                player.sendMessage(" Every player starts their life with only §c1/2 heart§r.");
                player.sendMessage(" §cMore hearts§r can be added by eating food you havent had before.");
                player.sendMessage("");
                break;
            case "spawning":
                player.sendMessage("");

                printHeading(player, "SPAWNING");
                player.sendMessage(" Players usually should spawn as a §cbaby§r to another random player.");
                player.sendMessage(" If there are no viable mothers, a player will spawn as an §cEve§r in the wilderness.");
                player.sendMessage(" Eves are §c10§r years old. They are the first in their lineage and can therefore chose their families name.");
                player.sendMessage("");
                break;
            case "baby":
                player.sendMessage("");

                printHeading(player, "BABY");
                player.sendMessage(" Babies can §cnot§r survive on their own.");
                player.sendMessage(" Use §cright-click§r with the §aBaby-Handler§r item to feed your child.");
                player.sendMessage(" While holding §aBaby-Handler§r in §cmainhand§r and standing close to baby, tap §csneak-button§r to carry baby.");
                player.sendMessage("");
                break;

            case "mother":
                player.sendMessage("");

                printHeading(player, "MOTHER");
                player.sendMessage(" Players can be chosen as mothers, if they are §c16-40§r years old.");
                player.sendMessage(" There is a §ccooldown§r after having a baby.");
                player.sendMessage(" Mothers can feed their babies by right-clicking with the §aBaby-Handler§r");
                player.sendMessage("");
                break;

            case "aging":
                player.sendMessage("");

                printHeading(player, "AGING");
                player.sendMessage(" Player age one §cyear§r per §cminute§r and live through multiple §cphases§r.");
                player.sendMessage(" Eventually you §owill§r die of §cold age§r.");
                player.sendMessage("");
                break;

            case "family":
                player.sendMessage("");

                printHeading(player, "FAMILY");
                player.sendMessage(" Every player is automatically member of a family.");
                player.sendMessage(" Family members cant attack each other.");
                player.sendMessage("");
                break;

            case "commands":
                player.sendMessage("");
               // player.sendMessage("§l§n§9-- COMMANDS --");
                printHeading(player, "COMMANDS");
                player.sendMessage("► [ §d/me§r ]");
                player.sendMessage("   Info about your life");
                player.sendMessage("► [ §d/you§r ]");
                player.sendMessage("   Used to change your babies name");
                player.sendMessage("► [ §d/family§r ]");
                player.sendMessage("   Info about your family");
                player.sendMessage("► [ §d/die§r ]");
                player.sendMessage("   If there are no other options...");
                player.sendMessage("");
                break;

            default:
                Notification.errorMsg(player, "Command not found");
        }

        return false;
    }

    private void printHeading(Player player, String text){
        player.sendMessage(  "§l§n§9-- " + text + " --");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("howto")) {
            if (sender instanceof Player) {
                l.add("about");
                l.add("spawning");
                l.add("aging");
                l.add("baby");
                l.add("mother");
                l.add("family");
                l.add("hearts");
                l.add("commands");

                return l;
            }
        }
        return l;
    }
}