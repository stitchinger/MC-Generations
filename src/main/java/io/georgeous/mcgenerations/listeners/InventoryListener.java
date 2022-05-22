package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void disableBabyHandlerDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (ItemManager.isBabyHandler(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableBabyItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (RoleManager.getInstance().isABaby(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void disableBabyInventoryOpening(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (RoleManager.getInstance().isABaby(player)) {
            Notification.errorMsg(player, "Babies cant interact with inventories");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableEnderchest(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            Notification.errorMsg(player, "Enderchests do not work here");
            event.setCancelled(true);
        }
    }
}