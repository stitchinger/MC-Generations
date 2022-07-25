package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.adoption.AdoptionManager;
import io.georgeous.mcgenerations.banish.BanishManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Logger;
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
import java.util.UUID;

public class BanishCommand implements CommandExecutor, TabCompleter {

    private final List<String> reportReasons = new ArrayList<>();

    public BanishCommand() {
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

        if(args.length == 3){
            UUID uuid = UUID.fromString(args[2]);
            if(args[1].equalsIgnoreCase("accept")){
                BanishManager.get().accept(uuid, player);
            }
            if(args[1].equalsIgnoreCase("decline")){
                BanishManager.get().decline(uuid, player);
            }
            return true;
        }

        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /banish <Name>");
            return true;
        }

        String arg = args[0].toLowerCase();
        player.sendMessage("You banished " + arg);

        Player playerToBanish = NickAPI.getPlayerOfNickedName(arg);
        Logger.log(playerToBanish.getName());
        BanishManager.get().requestBanish(player, playerToBanish);
        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();

        if (!(sender instanceof Player playerSender)) {
            return l;
        }


        if (cmd.getName().equalsIgnoreCase("banish")) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player != playerSender) {
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
