package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    private final MCG plugin;

    public InventoryListener() {
        this.plugin = MCG.getInstance();
    }

    @EventHandler
    public void stopBabyItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();
            PlayerRole role = RoleManager.get(player);
            if (role == null)
                return;
            PhaseManager phaseManager = RoleManager.get(player).pm;
            if (phaseManager == null)
                return;
            if (phaseManager.getCurrentPhase().getName().equalsIgnoreCase("baby")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cancelBabyHandlerDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (ItemManager.isBabyHandler(item)) {
            event.setCancelled(true);
        }
    }
}