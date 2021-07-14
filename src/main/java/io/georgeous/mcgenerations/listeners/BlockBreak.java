package io.georgeous.mcgenerations.listeners;


import io.georgeous.mcgenerations.MCG;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private MCG plugin;

    public BlockBreak(){
        this.plugin = MCG.getInstance();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getBlock().getType().equals(Material.DIAMOND_ORE)){

            Player player = event.getPlayer();
            player.sendMessage("You broke diamond ore");
            int amount = 0;

            if(plugin.data.getConfig().contains("players." + player.getUniqueId().toString() + ".total"))
                amount = plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".total");

            plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".total", (amount + 1));
            plugin.data.saveConfig();
        }
    }

}
