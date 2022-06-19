package io.georgeous.mcgenerations.commands.player;
import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class HowtoCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;


        if (args.length == 0) {
            // Summary
            player.sendMessage("");
            printHeading(player, "HOW TO");
            player.sendMessage("§f ► Hearts");
            player.sendMessage("§f ► Aging");
            player.sendMessage("§f ► Spawning");
            player.sendMessage("§f ► Baby");
            player.sendMessage("§f ► Mother");
            player.sendMessage("§f ► Family");
            player.sendMessage("§f ► Chat");
            player.sendMessage("§f ► Skin");
            player.sendMessage("§f ► Commands");
            //player.sendMessage("§6e.g. Use command §d[/howto hearts]§6 to find out, what's up with your hearts.");

            TextComponent howtoExampleCommand = new TextComponent("§d[/howto hearts]");
            howtoExampleCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/howto hearts"));
            howtoExampleCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Copy to Chat").color(ChatColor.GRAY).italic(true).bold(true).create()));
            player.spigot().sendMessage(
                    new TextComponent("§6e.g. Use command "),
                    howtoExampleCommand,
                    new TextComponent(" §6to find out, what's up with your hearts."));

            player.sendMessage("");
            return true;
        }

        String arg = args[0].toLowerCase();

        switch (arg) {

            case "hearts":
                player.sendMessage("");
                printHeading(player, "HEARTS");
                player.sendMessage(" Every player starts their life with only §c2 hearts§r.");
                player.sendMessage(" §cMore hearts§r can be added by eating food you havent had before.");
                player.sendMessage(" Establishing a §cvariety of foodsources§r ensures the survival of your family.");
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
                player.sendMessage(" While holding §aBaby-Handler§r in §cmainhand§r and standing close to baby, tap §csneak-button§r to summon Sven and carry your baby.");
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
                player.sendMessage(" Your §cskin§r changes to reflect your age.");
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

            case "chat":
                player.sendMessage("");
                printHeading(player, "CHAT");
                player.sendMessage(" There is §cno global§r server chat.");
                player.sendMessage(" You can only talk to players within a §crange of 100§r blocks.");
                player.sendMessage(" The color of a players firstname in chat is an §cindicator of the distance§r to that player.");
                player.sendMessage("");
                break;

            case "skin":
                player.sendMessage("");
                printHeading(player, "SKIN");
                player.sendMessage(" The skin represents your current age.");
                player.sendMessage(" Use ESC > Options > Skins Customization to get a more male/female look");
                player.sendMessage(" With §d[/namepreference <male/female>] §fyou can pick what kind of names you prefer");
                break;

            case "commands":
                player.sendMessage("");
                printHeading(player, "COMMANDS");
                player.sendMessage("► §d[/rules]§r");
                player.sendMessage("   Read the rules of the server");
                player.sendMessage("► §d[/me]§r");
                player.sendMessage("   Info about your life");
                player.sendMessage("► §d[/you]§r");
                player.sendMessage("   Used to change your babies name");
                player.sendMessage("► §d[/family]§r");
                player.sendMessage("   Info about your family");
                player.sendMessage("► §d[/babyhandler]§r");
                player.sendMessage("   Gives you a Baby-Handler, if you are old enough");
                player.sendMessage("► §d[/adopt]§r");
                player.sendMessage("   Invite other player into your family");
                player.sendMessage("► §d[/die]§r");
                player.sendMessage("   If there are no other options.. Abuse not allowed!");
                player.sendMessage("► §d[/namepreference]§r");
                player.sendMessage("   Get male or female names");
                player.sendMessage("► §d[/report]§r");
                player.sendMessage("   Report a player e.g.: /report Sue cheating");
                player.sendMessage("► §d[/shareign]§r");
                player.sendMessage("   Share your IGN with another player");
                player.sendMessage("► §d[/discord]§r");
                player.sendMessage("   Link to the discord");
                player.sendMessage("► §d[/ad]§r");
                player.sendMessage("   Get a advertising text, which you can use in the minehut hub");
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
                l.add("spawning");
                l.add("aging");
                l.add("baby");
                l.add("mother");
                l.add("family");
                l.add("hearts");
                l.add("chat");
                l.add("skin");
                l.add("commands");

                return l;
            }
        }
        return l;
    }
}