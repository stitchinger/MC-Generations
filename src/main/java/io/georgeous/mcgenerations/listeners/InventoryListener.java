package io.georgeous.mcgenerations.listeners;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;


public class InventoryListener implements Listener {
    Main plugin;

    public InventoryListener(){
        this.plugin = Main.getPlugin();
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent event){
        //if(event.getView().getTitle().contains(event.getPlayer().getName() + "'s Private Vault"))
           //this.plugin.menus.put(event.getPlayer().getUniqueId().toString(),event.getInventory().getContents());

    }


    @EventHandler
    public void onChangeMainHand(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        p.sendMessage("test");

        ItemStack one = new ItemStack(Material.STONE_BRICKS,1);
        ItemStack two = new ItemStack(Material.CRACKED_STONE_BRICKS,1);

        if(p.getInventory().getItem(0).isSimilar(one)){
            p.getInventory().setItem(0,two);
        }else{
            p.getInventory().setItem(0,one);
        }

    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player){
            Player p = ((Player) event.getEntity()).getPlayer();

            PlayerWrapper playerWrapper = PlayerManager.get(p);
            PlayerRole playerRole = playerWrapper.getRole();
            if(playerRole == null){
                return;
            }
            if(playerRole.am.ageInYears < 3){
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        PlayerWrapper cp = PlayerManager.get(p);
        if(event.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("Baby-Handler")){
            event.setCancelled(true);
            return;
        }
    }


}
