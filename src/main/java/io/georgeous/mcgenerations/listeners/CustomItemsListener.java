package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.RomanNumber;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CustomItemsListener implements Listener {

    @EventHandler
    public void onAnimalIncubator(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity target = event.getRightClicked();
        ItemStack usedItem = player.getInventory().getItemInMainHand();

        if(!ItemManager.isItemByName(usedItem, "Clone Egg"))
            return;


        if(!(target instanceof Cow) && !(target instanceof Sheep) && !(target instanceof Pig) && !(target instanceof Chicken)){
            return;
        }

        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.MASTER, 1,1);
        target.getWorld().spawnEntity(target.getLocation().clone().add(0.1,0.1,0.1), target.getType());
        usedItem.setAmount(0);
    }

    @EventHandler
    public void onSacrificialSwordAttack(EntityDeathEvent event){

        int threshold = 10;

        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();

        if(entity.getType() != EntityType.ZOMBIE && entity.getType() != EntityType.ZOMBIE_VILLAGER && entity.getType() != EntityType.HUSK && entity.getType() != EntityType.SKELETON && entity.getType() != EntityType.DROWNED && entity.getType() != EntityType.ZOMBIFIED_PIGLIN && entity.getType() != EntityType.SKELETON_HORSE ) return;

        if(player == null)
            return;

        ItemStack usedItem = player.getInventory().getItemInMainHand();

        if(usedItem.getType() != Material.GOLDEN_SWORD)
            return;

        ItemMeta meta = usedItem.getItemMeta();
        if(meta == null)
            return;

        if(!meta.hasCustomModelData()){
            meta.setCustomModelData(0);
        }

        int newModelData = meta.getCustomModelData() + 1;

        if(newModelData > threshold)
            return;

        meta.setCustomModelData(newModelData);

        if(newModelData >= threshold){
            meta.setDisplayName("Sacrificial Dagger");
            meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
        } else{
            meta.setDisplayName("Golden Sword " + RomanNumber.toRoman(newModelData));
        }

        usedItem.setItemMeta(meta);
        new BukkitRunnable(){
            @Override
            public void run() {
                player.getWorld().playSound(entity.getLocation(), Sound.PARTICLE_SOUL_ESCAPE, 2f,1);
                player.getInventory().getLocation();
                player.getWorld().spawnParticle(Particle.SOUL, entity.getLocation().clone().add(0,1,0), 10, 0.5, 1, 0.5,0);
            }
        }.runTaskLater(MCG.getInstance(), 20L);
    }
}
