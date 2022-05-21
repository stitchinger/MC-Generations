package io.georgeous.mcgenerations.systems.family;

import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FamilyCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerRole role = RoleManager.get(player);

        if (role == null) {
            return true;
        }



        Family family = role.getFamily();
        if (family == null) {
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Family: " + family.getColoredName());
            player.sendMessage("Members: " + family.memberCount());
            family.getMembers().forEach(member -> {
                player.sendMessage(" - " + member.getName());
            });
            if (family.getLeader() != null) {
                player.sendMessage("Leader: " + family.getLeader().getName());
            }
            return true;
        }

        switch (args[0]) {

            case "info":
                player.sendMessage("Family: " + family.getColoredName());
                player.sendMessage("Members: " + family.memberCount());
                family.getMembers().forEach(member -> {
                    player.sendMessage(" - " + member.getName());
                });
                if (family.getLeader() != null) {
                    player.sendMessage("Leader: " + family.getLeader().getName());
                }

                break;
            case "rename":
                attemptFamilyRename(role, family, args[1]);
                break;
            case "list":
                //player.sendMessage(familiesToString());
                //StringBuilder msg = new StringBuilder();
                for (Family f : FamilyManager.getAll()) {
                    player.sendMessage(f.getColoredName());
                    f.getMembers().forEach(member -> {
                        player.sendMessage(" - " + member.getName());
                    });
                    //msg.append(f.getColoredName()).append(", ");

                }

                break;
            default:
                Notification.errorMsg(player, "Command not found");
        }

        return false;
    }

    public void attemptFamilyRename(PlayerRole role, Family family, String name) {
        if (!role.pm.getCurrentPhase().getName().equalsIgnoreCase("child")) {
            Notification.errorMsg(role.getPlayer(), "You are too old to rename your Family");
            return;
        }

        if (family.isRenamed()) {
            Notification.errorMsg(role.getPlayer(), "Families can be renamed only one");
            return;
        }

        String last = name.substring(0, 1).toUpperCase() + name.substring(1);
        if (last.length() <= 2) {
            Notification.errorMsg(role.getPlayer(), "Families name is too short");
            return;
        }

        family.rename(last);
        Notification.successMsg(role.getPlayer(), "You changed your families name to " + family.getColoredName());
        role.updateScoreboard();
    }

    private String familiesToString() {
        StringBuilder msg = new StringBuilder();
        for (Family f : FamilyManager.getAll()) {
            msg.append(f.getColoredName()).append(", ");
        }
        return msg.toString();
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("family")) {
            if (sender instanceof Player) {
                l.add("list");
                l.add("rename");
                l.add("info");

                return l;
            }
        }
        return l;
    }
}