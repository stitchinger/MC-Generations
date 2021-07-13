package io.georgeous.mcgenerations.commands;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.family.Family;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.role.RoleManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameFamilyCommand implements CommandExecutor {

    private final Main main;

    public RenameFamilyCommand() {
        this.main = Main.getPlugin();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /renamefamily [new name]");
            return true;
        }

        Player player = (Player) sender;
        PlayerRole playerRole = RoleManager.get(player);

        if (playerRole == null) {
            sender.sendMessage("No PlayerRole attached");
            return true;
        }

        if (playerRole.getFamily() == null) {
            sender.sendMessage("No Family");
            return true;
        }

        if (!playerRole.pm.getCurrentPhase().getName().equalsIgnoreCase("child")) {
            sender.sendMessage("You are too old to rename your Family");
            return true;
        }

        Family family = playerRole.getFamily();

        if (family.isRenamed()) {
            sender.sendMessage("Families can be renamed only once");
            return true;
        }

        String last = args[0].substring(0, 1).toUpperCase() + args[0].substring(1);
        family.rename(last);

        playerRole.updateScoreboard();
        // todo update scoreboard for this player


        return true;
    }
}
