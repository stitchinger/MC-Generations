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
import xyz.haoshoku.nick.api.NickAPI;

import java.util.ArrayList;
import java.util.List;


public class ShareIgnCommand implements CommandExecutor, TabCompleter {

    private List<String> reportReasons = new ArrayList<>();

    public ShareIgnCommand(){
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

        PlayerRole role = RoleManager.get().get(player);

        if(role == null){
            Notification.errorMsg(player, "You need a role to use this");
            return true;
        }

        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /shareign <Name>");
            return true;
        }

        Player receiver = NickAPI.getPlayerOfNickedName(args[0]);

        if(receiver == null){
            Notification.errorMsg(player, "No player found");
            return true;
        }

        shareIgn(role, receiver, args[0]);

        return false;
    }

    private void shareIgn(PlayerRole sender, Player receiver, String receiverName){
        receiver.sendMessage(sender.getName()  + " IGN: " + sender.getPlayer().getName());
        Notification.successMsg(receiver, sender.getName()  + " shared their IGN with you: " + sender.getPlayer().getName());
        Notification.successMsg(sender.getPlayer(), "You shared your IGN with " + receiverName);
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();

        if (!(sender instanceof Player playerSender)) {
            return l;
        }

        if (cmd.getName().equalsIgnoreCase("shareign")) {
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

}