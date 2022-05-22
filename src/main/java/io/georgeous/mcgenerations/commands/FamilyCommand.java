package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
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
import java.util.Arrays;
import java.util.List;

public class FamilyCommand implements CommandExecutor, TabCompleter {

    public FamilyCommand() {
        CommandUtils.addTabComplete("family", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            Notification.onlyForPlayers(sender);
            return true;
        }

        PlayerRole role = RoleManager.getInstance().get(player);

        if (role == null) {
            Notification.errorMsg(player, "You do not have a role.");
            return true;
        }

        Family family = role.getFamily();
        if (family == null) {
            Notification.errorMsg(player, "You do not have a family.");
            return true;
        }

        switch (args[0]) {

            case "info":
                player.sendMessage("Family: " + family.getColoredName());
                player.sendMessage("Members: " + family.memberCount());
                family.getMembers().forEach(member -> player.sendMessage(" - " + member.getName()));
                if (family.getLeader() != null) {
                    player.sendMessage("Leader: " + family.getLeader().getName());
                }

                break;

            case "rename":
                attemptFamilyRename(role, family, args[1]);
                break;

            case "list":
                for (Family f : FamilyManager.getAll()) {
                    player.sendMessage(f.getColoredName());
                    f.getMembers().forEach(member -> player.sendMessage(" - " + member.getName()));
                }
                break;

            default:
                Notification.errorMsg(player, "Argument not found");
        }
        return true;
    }

    public void attemptFamilyRename(PlayerRole role, Family family, String name) {
        if (!role.getPhaseManager().getCurrentPhase().getName().equalsIgnoreCase("child")) {
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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();
        if (sender instanceof Player) {
            l.addAll(Arrays.asList("list", "rename", " info"));
        }
        return l;
    }
}