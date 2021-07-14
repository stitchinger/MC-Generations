package io.georgeous.mcgenerations.listeners;


import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.role.PlayerRole;
import io.georgeous.mcgenerations.role.RoleManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Interact implements Listener {

    private final MCG main;

    public Interact(MCG main) {
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
        if(baby instanceof Player){
            feeder.sendMessage(((Player) baby).getFoodLevel() + "");
            if(((Player) baby).getFoodLevel() < 20){
                feeder.setFoodLevel(feeder.getFoodLevel() - 2);

                Player targetPlayer = (Player) baby;
                int newFoodLevel = targetPlayer.getFoodLevel() + 5;
                targetPlayer.setFoodLevel(newFoodLevel);

                babyFeedEffect(baby.getWorld(), baby.getLocation());
            }

        }
    }

    public void babyFeedEffect(World world, Location location){
        world.spawnParticle(Particle.COMPOSTER,location,40,0.5,0.5,0.5);
        world.playSound(location, Sound.ENTITY_GENERIC_DRINK,1,1);
    }

    @EventHandler
    public void disableFriendlyFire(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player)
                || !(event.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Player receiver = (Player) event.getEntity();

        if(inSameFamily(damager, receiver)){
            event.setCancelled(true);
            friendlyFamilyTalk(RoleManager.get(damager), RoleManager.get(receiver));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        if(!(event.getEntity().getShooter() instanceof Player)){
            return;
        }

        if(!(event.getHitEntity() instanceof Player)){
            return;
        }

        Player damager = (Player) event.getEntity().getShooter();
        Player receiver = (Player) event.getHitEntity();

        if(inSameFamily(damager, receiver)){
            event.setCancelled(true);
            friendlyFamilyTalk(RoleManager.get(damager), RoleManager.get(receiver));
        }
    }



    public void friendlyFamilyTalk(PlayerRole damager, PlayerRole receiver){
        damager.getPlayer().sendMessage("You gave " + receiver.getName() + " a big hug");
        receiver.getPlayer().sendMessage(damager.getName() + " gave you a big hug");
    }

    public boolean inSameFamily(PlayerRole one, PlayerRole two){
        return one.getFamily() == two.getFamily();
    }

    public boolean inSameFamily(Player one, Player two){
        if(RoleManager.get(one) == null || PlayerManager.get(two) == null){
            return false;
        }
        PlayerRole roleOne = RoleManager.get(one);
        PlayerRole roleTwo = RoleManager.get(two);

        return inSameFamily(roleOne, roleTwo);
        //return roleOne.getFamily() == role.getFamily();
    }

}
