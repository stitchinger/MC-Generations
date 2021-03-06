package io.georgeous.mcgenerations.commands.player;

import io.georgeous.mcgenerations.commands.CommandUtils;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.BadWordFilter;
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

        PlayerRole role = RoleManager.get().get(player);

        if (role == null) {
            Notification.errorMsg(player, "You do not have a role.");
            return true;
        }

        Family family = role.getFamily();
        if (family == null) {
            Notification.errorMsg(player, "You do not have a family.");
            return true;
        }

        if (args.length == 0) {
            printFamilyInfo(player, family);
            return true;
        }



        switch (args[0]) {

            case "info":
                printFamilyInfo(player, family);

                break;

            case "rename":
                if (args.length == 1) {
                    Notification.errorMsg(player, "Usage: [/family rename <Name>]");
                    return true;
                }
                attemptFamilyRename(role, family, args[1]);
                break;

            case "list":
                player.sendMessage("");
                for (Family f : FamilyManager.getAll()) {
                    player.sendMessage(f.getColoredName());
                    f.getMembers().forEach(member -> player.sendMessage(" - " + member.getName()));
                }
                player.sendMessage("");

                break;

            default:
                Notification.errorMsg(player, "Argument not found");
        }
        return true;
    }

    private void printFamilyInfo(Player player, Family family){
        player.sendMessage("");
        Notification.neutralMsg(player, "Family: " + family.getColoredName());
        Notification.neutralMsg(player, "Members: " + family.memberCount());

        family.getMembers().forEach(member -> {
            player.sendMessage("  ???  " + member.getName());
        });
        if (family.getLeader() != null) {
            Notification.neutralMsg(player, "Leader: " + family.getLeader().getName());
        }
        player.sendMessage("");
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

        if (last.length() < 3) {
            Notification.errorMsg(role.getPlayer(), "Family name cant be shorter than 3 characters!");
            return;
        }

        if(last.length() > 14){
            Notification.errorMsg(role.getPlayer(), "Family name cant be longer than 14 characters!");
            return;
        }

        if(!isValidName(last)){
            Notification.errorMsg(role.getPlayer(), "Special characters are not allowed!");
            return;
        }

        if(BadWordFilter.getCensoredText(last) != last){
            Notification.errorMsg(role.getPlayer(), "Profanity is not allowed in names!");
            return;
        }

        family.rename(last);
        Notification.successMsg(role.getPlayer(), "You changed your families name to " + family.getColoredName());
        role.updateScoreboard();
    }

    public static boolean isValidName(String str) {
        String expression = "^[a-zA-Z\\s]+";
        return str.matches(expression);
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