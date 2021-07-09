package io.georgeous.mcgenerations.listeners;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.role.RoleManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

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

        if(ItemManager.isBabyHandler(usedItem) && target instanceof LivingEntity){
            feedBaby(player, (LivingEntity) target);
        }
    }

    public void feedBaby(Player feeder, LivingEntity baby){
        feeder.setFoodLevel(feeder.getFoodLevel() - 2);

        if(baby instanceof Player){
            Player targetPlayer = (Player) baby;
            int newFoodLevel = targetPlayer.getFoodLevel() + 5;
            targetPlayer.setFoodLevel(newFoodLevel);
        }
        babyFeedEffect(baby.getWorld(), baby.getLocation());
    }

    public void babyFeedEffect(World world, Location location){
        world.spawnParticle(Particle.COMPOSTER,location,40,0.5,0.5,0.5);
        world.playSound(location, Sound.ENTITY_GENERIC_DRINK,1,1);
    }

    @EventHandler
    public void disableFriendlyFire(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

            Player damager = (Player) event.getDamager();
            Player receiver = (Player) event.getEntity();

            if(RoleManager.get(damager) == null || PlayerManager.get(receiver) == null){
                return;
            }

            PlayerRole damagerRole = RoleManager.get(damager);
            PlayerRole receiverRole = RoleManager.get(receiver);

            if(inSameFamily(damagerRole, receiverRole)){
                event.setCancelled(true);
                friendlyFamilyTalk(damagerRole, receiverRole);
            }
        }
    }

    public void friendlyFamilyTalk(PlayerRole damager, PlayerRole receiver){
        damager.getPlayer().sendMessage("You gave " + receiver.getName() + " a big hug");
        receiver.getPlayer().sendMessage(damager.getName() + " gave you a big hug");
    }

    public boolean inSameFamily(PlayerRole one, PlayerRole two){
        return one.getFamily() == two.getFamily();
    }

}
