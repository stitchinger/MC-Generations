package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.files.Reporter;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ReportCommand implements CommandExecutor, TabCompleter {

    private final List<String> reportReasons = new ArrayList<>();

    public ReportCommand(){
        reportReasons.add("Cheating");
        reportReasons.add("Griefing");
        reportReasons.add("Exploiting");
        reportReasons.add("Profanity");
        reportReasons.add("Bullying");
        reportReasons.add("Other");
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only for players");
            return true;
        }

        if (args.length > 2 || args.length == 0) {
            Notification.errorMsg(player, "Usage: /report <Name> <Reason>");
            return true;
        }

        //String arg = args[0].toLowerCase();

        switch (args[0]) {


            case "list":
                if (!player.isOp()) {
                    Notification.errorMsg(player, "Only for OPs");
                    return true;
                }
                Reporter.printReports(player);
                break;

            case "delete":
                if (!player.isOp()) {
                    Notification.errorMsg(player, "Only for OPs");
                    return true;
                }

                if(args.length != 2){
                    return true;
                }

                boolean result = Reporter.deleteReport(args[1]);
                if(result){
                    Notification.successMsg(player, "Deleted all reports of " + args[1]);
                }else{
                    Notification.errorMsg(player, "Couldnt find any reports of " + args[1]);
                }

            case "reload":
                if (!player.isOp()) {
                    Notification.errorMsg(player, "Only for OPs");
                    return true;
                }
                Reporter.reload();
                break;

            default:
                if (args.length != 2) {
                    Notification.errorMsg(player, "Usage: /report <Name> <Reason>");
                    return true;
                }

                if(!containsCaseInsensitive(args[1], reportReasons)){
                    Notification.errorMsg(player, "Please pick a reason from the list");
                    return true;
                }
                Reporter.reportPlayer(player, args[0], args[1]);
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();

        if (!(sender instanceof Player playerSender)) {
            return l;
        }

        if (cmd.getName().equalsIgnoreCase("report")) {
            if(args[0].equalsIgnoreCase("delete")){
                return Reporter.getReportedPlayers();
            }

            if(!args[0].equals("")){
                return reportReasons;
            }


            Bukkit.getOnlinePlayers().forEach(player -> {
                if(player != playerSender ){
                    PlayerRole role = RoleManager.get().get(player);
                    if (role != null) {
                        l.add(role.getName());
                    } else {
                        l.add(player.getName());
                    }
                }
            });
            return l;
        }

        return l;
    }

    public boolean containsCaseInsensitive(String s, List<String> l){
        for (String string : l){
            if (string.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }
}