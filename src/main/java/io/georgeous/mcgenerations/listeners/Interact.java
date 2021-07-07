package io.georgeous.mcgenerations.listeners;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Interact implements Listener {

    private final Main main;

    public Interact(Main main) {
        this.main = main;
    }


    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity target = event.getRightClicked();

        ItemStack usedItem = player.getInventory().getItemInMainHand();
        if(usedItem.getItemMeta().getDisplayName().contains("Baby-Handler") && target instanceof LivingEntity){
            player.setFoodLevel(player.getFoodLevel() - 2);

            if(target instanceof Player){
                ((Player) target).setFoodLevel(((Player) target).getFoodLevel() + 5);
            }

            // Effects
            target.getWorld().spawnParticle(Particle.COMPOSTER,target.getLocation(),40,0.5,0.5,0.5);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_DRINK,1,1);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player receiver = (Player) event.getEntity();
            if(PlayerManager.get(damager).getRole() == null || PlayerManager.get(receiver).getRole() == null){
                return;
            }
            if(PlayerManager.get(damager).getRole().getFamily() == PlayerManager.get(receiver).getRole().getFamily()){
                event.setCancelled(true);
                damager.sendMessage("You gave " + PlayerManager.get(receiver).getRole().getName() + " a big hug");
                receiver.sendMessage(PlayerManager.get(damager).getRole().getName() + " gave you a big hug");
            }
        }
    }

}
