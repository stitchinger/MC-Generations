package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void disableBabyHandlerDrop(PlayerDropItemEvent event) {
        event.getPlayer().sendMessage("NO can doo");
        ItemStack item = event.getItemDrop().getItemStack();
        if (ItemManager.isBabyHandler(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableBabyItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (RoleManager.get().isABaby(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void disableBabyInventoryOpening(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (RoleManager.get().isABaby(player)) {
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

    @EventHandler
    public void disableBabyHandlerMove(InventoryClickEvent event) {
        if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
            if(ItemManager.isBabyHandler(event.getCurrentItem()) && event.getClickedInventory() == event.getWhoClicked().getInventory()) {
                event.setCancelled(true);
            }
        }else if(event.getAction() == InventoryAction.PLACE_ALL){
            if(ItemManager.isBabyHandler(event.getCursor()) && event.getClickedInventory() != event.getWhoClicked().getInventory()) {
                event.setCancelled(true);
            }
        }
    }
}