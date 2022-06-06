package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FriendlyTalk;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Interact implements Listener {

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
            babyFullEffect(baby.getLocation());
            return;
        }

        feeder.setFoodLevel(Math.max(feeder.getFoodLevel() - 2, 0));
        int newFoodLevel = Math.min(baby.getFoodLevel() + 5, 20);
        baby.setFoodLevel(newFoodLevel);

        babyRole.babyFeedEffect();
    }

    public void babyFullEffect(Location location) {
        World world = location.getWorld();
        if (world != null) {
            world.playSound(location, Sound.ENTITY_VILLAGER_NO, 1, 2);
        }
    }

    @EventHandler
    public void disableFriendlyFire(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) || !(event.getEntity() instanceof Player receiver))
            return;

        PlayerRole damagerRole = RoleManager.get().get(damager);
        PlayerRole receiverRole = RoleManager.get().get(receiver);

        if(damagerRole == null || receiverRole == null){
            event.setCancelled(true);
            return;
        }

        if (Family.inSameFamily(damager, receiver)) {
            event.setCancelled(true);
            friendlyFamilyTalk(damagerRole, receiverRole);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player damager) || !(event.getHitEntity() instanceof Player receiver)) {
            return;
        }

        PlayerRole damagerRole = RoleManager.get().get(damager);
        PlayerRole receiverRole = RoleManager.get().get(receiver);

        if(damagerRole == null || receiverRole == null){
            event.setCancelled(true);
            return;
        }

        if (Family.inSameFamily(damager, receiver)) {
            event.setCancelled(true);
            friendlyFamilyTalk(damagerRole, receiverRole);
        }
    }





    public void friendlyFamilyTalk(PlayerRole damager, PlayerRole receiver) {
        Player pd = damager.getPlayer();
        Player pr = receiver.getPlayer();

        if (!FriendlyTalk.isCoolDown(pd)) {
            String[] messages = FriendlyTalk.getMessages(damager.getName(), receiver.getName());

            Notification.neutralMsg(pd, messages[0]);
            Notification.neutralMsg(pr, messages[1]);

            try {
                pd.getWorld().spawnParticle(Particle.HEART, pd.getLocation(), 5, 0.5, 0.5, 0.5);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            FriendlyTalk.addCoolDown(pd);
        }
    }

    @EventHandler
    public void disableBabyBlockPlacement(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerRole role = RoleManager.get().get(player);
        if (role == null)
            return;
        PhaseManager phaseManager = RoleManager.get().get(player).getPhaseManager();
        if (phaseManager == null)
            return;
        if (phaseManager.getCurrentPhase().getName().equalsIgnoreCase("baby")) {
            event.setCancelled(true);
        }
    }
}