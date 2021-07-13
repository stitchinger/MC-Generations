package io.georgeous.mcgenerations.commands;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.role.RoleManager;
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

        Player player = (Player) sender;

        if(!player.isOp()){
            sender.sendMessage("This command is only for OPs");
            return true;
        }


        if(args.length == 0){
            sender.sendMessage("Usage: /Age 13");
        }

        if(args.length == 1){

            PlayerRole playerRole = RoleManager.get(player);
            if(playerRole == null){
                player.sendMessage("No PlayerRole attached");
                return true;
            }
            playerRole.am.setAge(Integer.parseInt(args[0]));
            //cp.pm.checkPhaseUp();

            player.sendMessage("Changed " + player.getName() + "'s age to " + Integer.parseInt(args[0]));

        }
        return true;
    }
}
