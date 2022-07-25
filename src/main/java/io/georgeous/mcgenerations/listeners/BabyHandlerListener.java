package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Logger;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class BabyHandlerListener implements Listener {

    @EventHandler
    public void onBabyFeed(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity target = event.getRightClicked();
        ItemStack usedItem = player.getInventory().getItemInMainHand();

        if (target instanceof Player && ItemManager.isBabyHandler(usedItem)) {
            feedBaby(player, (Player) target);
        }
    }

    public void feedBaby(Player feeder, Player baby) {
        PlayerRole babyRole = RoleManager.get().get(baby);
        if (babyRole == null || (baby.getFoodLevel() >= 20 && feeder.getFoodLevel() > 0) || !babyRole.getPhaseManager().getCurrentPhase().isFeedable())
            return;

        if (baby.getFoodLevel() >= 18){
            Logger.log("baby full");
            babyFullEffect(baby.getLocation());
            return;
        }

        float refillValue = 6f;
        float costFactor = 0.5f;

        float foodToFill = Math.min(20 - baby.getFoodLevel(), refillValue);
        float cost = Math.max((foodToFill * costFactor),1);

        feeder.setFoodLevel(Math.max(feeder.getFoodLevel() - (int)cost, 0));
        int newFoodLevel = Math.min(baby.getFoodLevel() + (int)foodToFill, 20);
        baby.setFoodLevel(newFoodLevel);

        babyFeedEffect(babyRole.getPlayer());
    }

    public void babyFeedEffect(Player player) {
        Location location = player.getLocation();
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.COMPOSTER, location, 40, 0.5, 0.5, 0.5);
            world.playSound(location, Sound.ENTITY_GENERIC_DRINK, 1, 1);
        }
    }

    public void babyFullEffect(Location location) {
        World world = location.getWorld();
        if (world != null) {
            world.playSound(location, Sound.ENTITY_VILLAGER_NO, 1, 2);
        }
    }

    @EventHandler
    public void onBabyHandlerItemFrame(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();

        if(!(event.getRightClicked() instanceof ItemFrame))
            return;

        ItemStack usedItem = player.getInventory().getItemInMainHand();

        if(usedItem.getType().equals(Material.AIR)){
            usedItem = player.getInventory().getItemInOffHand();
        }

        if(ItemManager.isBabyHandler(usedItem)){
            event.setCancelled(true);
            Notification.errorMsg(player, "You cant put the Baby-Handler in an Item Frame");
        }
    }

    @EventHandler
    public void disableBabyHandlerMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
            if(ItemManager.isBabyHandler(event.getCurrentItem()) && event.getClickedInventory() == event.getWhoClicked().getInventory()) {
                Notification.errorMsg(player, "You cant move the Baby-Handler in another inventory");
                event.setCancelled(true);
            }
        }else if(event.getAction() == InventoryAction.PLACE_ALL){
            if(ItemManager.isBabyHandler(event.getCursor()) && event.getClickedInventory() != event.getWhoClicked().getInventory()) {
                Notification.errorMsg(player, "You cant move the Baby-Handler in another inventory");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void disableBabyHandlerDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (ItemManager.isBabyHandler(item)) {
            Notification.errorMsg(event.getPlayer(), "You cant drop your Baby-Handler");
            event.setCancelled(true);
        }
    }
}
