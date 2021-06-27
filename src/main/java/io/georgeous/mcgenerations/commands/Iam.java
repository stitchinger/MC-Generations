package io.georgeous.mcgenerations.commands;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
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

        if (args.length != 3) {
            sender.sendMessage("Usage: /I am Lisa Simpson");
        }

        if (args.length == 3) {
            Player p = (Player) sender;
            PlayerWrapper cp = PlayerManager.get(p);

            if(!cp.playerRole.isNamed()){
                String first = args[1].substring(0, 1).toUpperCase() + args[1].substring(1);
                String last = args[2].substring(0, 1).toUpperCase() + args[2].substring(1);

                cp.playerRole.setName(first);
                cp.playerRole.family.setName(last);
                cp.playerRole.setNamed(true);
            } else{
                p.sendMessage("You can name yourself only once");
            }





        }


        return true;
    }
}
