package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.adoption.AdoptionManager;
import io.georgeous.mcgenerations.files.Reporter;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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


public class AdoptCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only for players");
            return true;
        }


        if(args.length == 3){
            UUID uuid = UUID.fromString(args[2]);
            if(args[1].equalsIgnoreCase("accept")){
                AdoptionManager.get().accept(uuid, player);
            }
            if(args[1].equalsIgnoreCase("decline")){
                AdoptionManager.get().decline(uuid, player);
            }
            return true;
        }


        if (args.length != 1 ) {
            Notification.errorMsg(player, "Usage: /adopt <Name>");
            return true;
        }

        Player playerToAdopt = NickAPI.getPlayerOfNickedName(args[0]);
        //adoptation(player, playerToAdopt);

        AdoptionManager.get().requestAdoptation(player, playerToAdopt);

        return false;
    }

    private void adoptation(Player adopter, Player adoptee){
        if(adoptee == null){
            Notification.errorMsg(adopter, "No player found with this name");
            return;
        }

        PlayerRole adopterRole = RoleManager.get().get(adopter);
        PlayerRole adopteeRole = RoleManager.get().get(adoptee);

        Family adopterFamily = adopterRole.getFamily();
        Family adopteeFamily = adopteeRole.getFamily();

        if(adopterFamily == adopteeFamily){
            Notification.errorMsg(adopter, adopteeRole.getName() + " is already in your family");
            return;
        }

        adopteeRole.getFamily().removeMember(adopteeRole);
        adopterRole.getFamily().addMember(adopteeRole);
        adopteeRole.updateScoreboard();

        adoptee.teleport(adopter.getLocation());
        Notification.successMsg(adopter, "You adopted " + adopteeRole.getName() + ".");


    }



    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();

        if (!(sender instanceof Player playerSender)) {
            return l;
        }

        if (cmd.getName().equalsIgnoreCase("adopt")) {

            Bukkit.getOnlinePlayers().forEach(player -> {
                if(player != playerSender ){
                    PlayerRole role = RoleManager.get().get(player);
                    if (role != null) {
                        l.add(role.getName());
                    }
                }
            });
            return l;
        }

        return l;
    }
}