package io.georgeous.mcgenerations.listeners;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.role.RoleManager;
import io.georgeous.mcgenerations.player.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryListener implements Listener {
    private final Main plugin;

    public InventoryListener(){
        this.plugin = Main.getPlugin();
    }

    @EventHandler
    public void stopBabyItemPickup(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player){
            Player player = ((Player) event.getEntity()).getPlayer();
            PlayerRole role = RoleManager.get(player);
            if(role == null)
                return;
            PhaseManager phaseManager = RoleManager.get(player).pm;
            if(phaseManager == null)
                return;
            if(phaseManager.getCurrentPhase().getName().equalsIgnoreCase("baby")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cancelBabyHandlerDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if(ItemManager.isBabyHandler(item)){
            event.setCancelled(true);
        }
    }

}
