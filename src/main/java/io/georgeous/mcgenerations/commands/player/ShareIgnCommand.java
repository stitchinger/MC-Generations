package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.files.Reporter;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.haoshoku.nick.api.NickAPI;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;


public class ShareIgnCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only for players");
            return true;
        }

        PlayerRole role = RoleManager.get().get(player);

        if (args.length != 1) {
            Notification.errorMsg(player, "Usage: /shareign <Name>");
            return true;
        }

        Player receiver = NickAPI.getPlayerOfNickedName(args[0]);

        if(receiver == null){
            Notification.errorMsg(player, "No player found");
            return true;
        }

        shareIgn(player, receiver, args[0]);

        return false;
    }

    private void shareIgn(Player sender, Player receiver, String receiverName){
        PlayerRole role = RoleManager.get().get(sender);
        String name = sender.getName();
        if(role != null){
            name = role.getName();
        }
        //receiver.sendMessage(sender.getName()  + " IGN: " + sender.getPlayer().getName());

        TextComponent text = new TextComponent(name + " shared their IGN with you: ");

        TextComponent clickName = new TextComponent(sender.getName());
        clickName.setColor(ChatColor.BLUE);
        clickName.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, sender.getName()));
        clickName.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to copy!").color(ChatColor.GRAY).italic(true).create())
        );
        //Notification.successMsg(receiver, component);
        Notification.successMsg(receiver, text, clickName);
        Notification.successMsg(sender, "You shared your IGN with " + receiverName);
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