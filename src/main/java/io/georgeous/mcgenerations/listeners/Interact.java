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
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem() != null){
            if(event.getItem().getType() == Material.CARROT_ON_A_STICK && event.getHand() == EquipmentSlot.HAND){
                //event.getPlayer().sendMessage("Carrot on a stick on anything");
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        PlayerWrapper cp = PlayerManager.get(player);
        Entity target = event.getRightClicked();


        double distance = target.getLocation().distance(player.getLocation());
        ItemStack usedItem = player.getInventory().getItemInMainHand();

        if(usedItem.getItemMeta().getDisplayName().contains("Baby-Handler") && target instanceof LivingEntity){
            target.getWorld().spawnParticle(Particle.COMPOSTER,target.getLocation(),40,0.5,0.5,0.5);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_DRINK,1,1);

            PotionEffect hunger = new PotionEffect(PotionEffectType.HUNGER,60,100,false,false,false);
            player.addPotionEffect(hunger);

            PotionEffect saturation = new PotionEffect(PotionEffectType.SATURATION,120,100,false,false,false);
            ((LivingEntity) target).addPotionEffect(saturation);

            //player.sendMessage(distance + "");
        }

    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        //event.getPlayer().sendMessage("PlayerInteractAtEntityEvent");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            damager.sendMessage("nope");
        }
    }

}
