package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FriendlyTalk;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateEntity;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.*;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class Interact implements Listener {

    /*
    FRIENDLY FIRE
     */

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

        boolean hasSword = damagerRole.getPlayer().getInventory().getItemInMainHand().getType().name().endsWith("SWORD");
        boolean isAdult = damagerRole.getPhaseManager().getCurrentPhase().getId() >= 4;

        if( isAdult && hasSword ){
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

    private void friendlyFamilyTalk(PlayerRole damager, PlayerRole receiver) {
        Player pd = damager.getPlayer();
        Player pr = receiver.getPlayer();

        if (!FriendlyTalk.isCoolDown(pd)) {
            String[] messages = FriendlyTalk.getMessages(damager.getName(), receiver.getName());

            Notification.neutralMsg(pd, messages[0]);
            Notification.neutralMsg(pr, messages[1]);

            try {
                pd.getWorld().spawnParticle(Particle.HEART, pd.getLocation().clone().add(0,2,0), 5, 0.5, 0.5, 0.5);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            FriendlyTalk.addCoolDown(pd);
        }
    }

    /*
    Baby Handicaps
     */

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

    @EventHandler
    public void disableBabyHarvest(BlockBreakEvent event){
        Player player = event.getPlayer();
        PlayerRole role = RoleManager.get().get(player);
        if(role == null){
            return;
        }
        if(role.getPhaseManager().getCurrentPhase().getId() != 0){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void preventGraveDrop(BlockBreakEvent event){
        Player player = event.getPlayer();
        PlayerRole role = RoleManager.get().get(player);

        BlockState blockState = event.getBlock().getState();
        if (!(blockState instanceof Sign))
            return;

        Sign s = (Sign) blockState;

        if(!(s.getLine(3).toLowerCase().startsWith("r.i.p.")))
            return;

        event.setDropItems(false);
        s.getLocation().getWorld().dropItemNaturally(s.getLocation(),new ItemStack(Material.STICK, 2));
    }

    @EventHandler
    public void disableBabyBoat(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        PlayerRole role = RoleManager.get().get(player);

        if(role == null)
            return;

        if(role.getPhaseManager().getCurrentPhase().getId() > 1)
            return;

        if(!event.getRightClicked().getType().equals(EntityType.BOAT) &&
                !event.getRightClicked().getType().equals(EntityType.MINECART) &&
                !event.getRightClicked().getType().equals(EntityType.MULE) &&
                !event.getRightClicked().getType().equals(EntityType.DONKEY) &&
                !event.getRightClicked().getType().equals(EntityType.HORSE))
            return;

        event.setCancelled(true);
        Notification.errorMsg(player, "You are too young to do this");
    }

    /*
    SURROGATE
     */

    @EventHandler
    public void onSurrogateMount(EntityMountEvent event){
        // If player with surro mounts entity, add surro as passenger
        // Avoiding surro to hover
        if(!(event.getEntity() instanceof Player player)){
            return;
        }

        SurrogateEntity surro = SurrogateManager.getInstance().getSurrogateOfPlayer(player);
        if(surro == null){
            return;
        }

        event.getMount().addPassenger(surro.getEntity());
        surro.getEntity().addScoreboardTag("riding");
        Logger.log("testing 124");
    }

    @EventHandler
    public void onSurrogateDismount(EntityDismountEvent event){
        if(!event.getEntity().getScoreboardTags().contains("surrogate"))
            return;

        if(!event.getEntity().getScoreboardTags().contains("riding"))
            return;

        event.getEntity().removeScoreboardTag("riding");
    }

   /*
    SIGNS
     */

    @EventHandler
    public void onSignBadword(SignChangeEvent event){
        for(int i = 0; i < event.getLines().length; i++){
            event.setLine(i, BadWordFilter.getCensoredText(event.getLine(i)));
        }
    }

}