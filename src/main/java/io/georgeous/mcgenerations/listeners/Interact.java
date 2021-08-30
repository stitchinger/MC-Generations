package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Interact implements Listener {

    private final MCG main;
    HashMap<String, Long> friendlyTalkCooldown = new HashMap<>();

    public Interact(MCG main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity target = event.getRightClicked();
        ItemStack usedItem = player.getInventory().getItemInMainHand();

        if (ItemManager.isBabyHandler(usedItem) && target instanceof Player) {
            feedBaby(player, (Player) target);
        }
    }

    public void feedBaby(Player feeder, Player baby) {
        PlayerRole role = RoleManager.get(baby);
        if (role == null)
            return;

        if (baby.getFoodLevel() >= 20)
            return;

        if (!role.pm.getCurrentPhase().isFeedable())
            return;

        feeder.setFoodLevel(feeder.getFoodLevel() - 2);
        int newFoodLevel = Math.min(baby.getFoodLevel() + 5, 20);
        baby.setFoodLevel(newFoodLevel);

        babyFeedEffect(baby.getLocation());
    }

    public void babyFeedEffect(Location location) {
            World world = location.getWorld();
            world.spawnParticle(Particle.COMPOSTER, location, 40, 0.5, 0.5, 0.5);
            world.playSound(location, Sound.ENTITY_GENERIC_DRINK, 1, 1);
        }

    @EventHandler
    public void disableFriendlyFire(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)
                || !(event.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Player receiver = (Player) event.getEntity();

        if (Family.inSameFamily(damager, receiver)) {
            event.setCancelled(true);
            friendlyFamilyTalk(RoleManager.get(damager), RoleManager.get(receiver));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        if (!(event.getHitEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getEntity().getShooter();
        Player receiver = (Player) event.getHitEntity();

        if (Family.inSameFamily(damager, receiver)) {
            event.setCancelled(true);
            friendlyFamilyTalk(RoleManager.get(damager), RoleManager.get(receiver));
        }
    }

    public void friendlyFamilyTalk(PlayerRole damager, PlayerRole receiver) {
        //FriendlyTalk ft = new FriendlyTalk(damager, receiver);
        damager.getPlayer().sendMessage("You gave " + receiver.getName() + " a big hug");
        receiver.getPlayer().sendMessage(damager.getName() + " gave you a big hug");
    }

    @EventHandler
    public void disableBabyBlockPlacement(BlockPlaceEvent event) {
        Player player = event.getPlayer();
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