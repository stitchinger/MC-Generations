package io.georgeous.mcgenerations.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,  Command command,  String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if(!player.isOp()){
            sender.sendMessage("This command is only for OPs");
            return true;
        }

        if(player.getGameMode().equals(GameMode.CREATIVE)){
            player.setGameMode(GameMode.SURVIVAL);
        } else{
            player.setGameMode(GameMode.CREATIVE);
        }


        return true;
    }
}