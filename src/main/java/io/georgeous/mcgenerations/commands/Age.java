package io.georgeous.mcgenerations.commands;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Age implements CommandExecutor {

    private final Main main;

    public Age(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This command is for players only!");
            return true;
        }

        if(args.length == 0){
            sender.sendMessage("Usage: /Age 13");
        }

        if(args.length == 1){
            Player p = (Player) sender;
            PlayerWrapper cp = PlayerManager.get(p);
            if(cp.playerRole == null){
                p.sendMessage("No PlayerRole attached");
                return true;
            }
            cp.playerRole.am.setAge(Integer.parseInt(args[0]));
            //cp.pm.checkPhaseUp();

            p.sendMessage("Changed " + p.getName() + "'s age to " + Integer.parseInt(args[0]));

        }



        return true;
    }
}
