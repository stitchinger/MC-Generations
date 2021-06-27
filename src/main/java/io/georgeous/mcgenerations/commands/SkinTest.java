package io.georgeous.mcgenerations.commands;

import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SkinTest implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This command is for players only!");
            return true;
        }

        Player p = (Player) sender;
        PlayerWrapper cp = PlayerManager.get(p);

        if(args.length == 1){
            //NameManager.addPlayer(p);
            //NameManager.map.get(p).changeSkinToMineSkinId(args[0]);
            String skinID = "";
            String arg = args[0];
            if(arg.contains("child")){
                skinID = "2007359867";
            }else if(arg.contains("teen")){
                skinID = "297371";

            }else if(arg.contains("adult")){
                skinID = "584227931";

            }else if(arg.contains("elder")){
                skinID = "1144027445";
            }else{
                skinID = arg;
            }
            //NickManager nm = new NickManager(p);
            //nm.changeSkinToMineSkinId(skinID);
        }



        return false;
    }
}
