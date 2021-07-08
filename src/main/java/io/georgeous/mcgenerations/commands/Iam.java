package io.georgeous.mcgenerations.commands;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Iam implements CommandExecutor {

    private final Main main;

    public Iam(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }

        Player player = (Player) sender;
        PlayerWrapper playerWrapper = PlayerManager.get(player);
        PlayerRole playerRole = playerWrapper.getRole();

        if(playerRole == null){
            sender.sendMessage("No PlayerRole attached");
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage("Usage: /I am Lisa Simpson");
        }

        if (args.length == 3) {
            if(!playerRole.isNamedByMother()){
                String first = args[1].substring(0, 1).toUpperCase() + args[1].substring(1);
                String last = args[2].substring(0, 1).toUpperCase() + args[2].substring(1);

                playerRole.rename(first);
                playerRole.family.rename(last);
            } else{
                player.sendMessage("You can name yourself only once");
            }
        }
        return true;
    }
}
