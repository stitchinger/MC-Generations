package io.georgeous.mcgenerations.commands.player;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
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


public class RulesCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        PlayerWrapper playerWrapper = PlayerManager.get().getWrapper(player);



        if (args.length == 0) {
            if(playerWrapper != null){
                playerWrapper.setRulesRead(true);
            }
            // Summary
            player.sendMessage("");
            printHeading(player, "RULES");
            player.sendMessage("§f ► No cheating");
            player.sendMessage("§f ► No griefing");
            player.sendMessage("§f ► No abusing of §d[/die]");
            player.sendMessage("§f ► No offensive language");
            player.sendMessage("§f ► No sniping of coordinates");
            player.sendMessage("§f All of these rules are a reason for a ban.");
            player.sendMessage("§f Please use §d[/rules accept]§f if you read and accept the rules.");

            player.sendMessage("");
            return true;
        }

        String arg = args[0].toLowerCase();

        switch (arg) {

            case "accept":

                if(playerWrapper != null){
                    if(playerWrapper.getRulesRead()){
                        playerWrapper.setRulesAccepted(true);
                        Notification.successMsg(player, "Thanks for accepting our rules. Have fun ;)");
                    } else{
                        Notification.errorMsg(player,"You have to read the rules, before you can accept them! Use §d[/rules]");
                    }

                }

                break;


            default:
                Notification.errorMsg(player, "Command not found");
        }

        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("rules")) {
            if (sender instanceof Player) {
                l.add("accept");

                return l;
            }
        }
        return l;
    }

    private void printHeading(Player player, String text){
        player.sendMessage(  "§l§n§9-- " + text + " --");
    }

}