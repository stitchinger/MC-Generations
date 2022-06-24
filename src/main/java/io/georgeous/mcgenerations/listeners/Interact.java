package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FriendlyTalk;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateEntity;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

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
            Logger.log("baby full");
            babyFullEffect(baby.getLocation());
            return;
        }

        float refillValue = 6f;
        float costFactor = 0.5f;

        float foodToFill = Math.min(20 - baby.getFoodLevel(), refillValue);
        float cost = Math.max((foodToFill * costFactor),1);

        feeder.setFoodLevel(Math.max(feeder.getFoodLevel() - (int)cost, 0));
        int newFoodLevel = Math.min(baby.getFoodLevel() + (int)foodToFill, 20);
        baby.setFoodLevel(newFoodLevel);

        babyFeedEffect(babyRole.getPlayer());
    }

    public void babyFeedEffect(Player player) {
        Location location = player.getLocation();
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.COMPOSTER, location, 40, 0.5, 0.5, 0.5);
            world.playSound(location, Sound.ENTITY_GENERIC_DRINK, 1, 1);
        }
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


    public void friendlyFamilyTalk(PlayerRole damager, PlayerRole receiver) {
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
    public void onSurrogateMount(EntityMountEvent event){
        if(!(event.getEntity() instanceof Player player)){
            return;
        }

        SurrogateEntity surro = SurrogateManager.getInstance().getSurrogateOfPlayer(player);
        if(surro == null){
            return;
        }

        event.getMount().addPassenger(surro.getEntity());
        surro.getEntity().addScoreboardTag("riding");
    }

    @EventHandler
    public void onSurrogateDismount(EntityDismountEvent event){
        if(!event.getEntity().getScoreboardTags().contains("surrogate"))
            return;

        if(!event.getEntity().getScoreboardTags().contains("riding"))
            return;


        event.getEntity().removeScoreboardTag("riding");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){

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
    public void onSignBadword(SignChangeEvent event){
        for(int i = 0; i < event.getLines().length; i++){
            event.setLine(i, BadWordFilter.getCensoredText(event.getLine(i)));
        }
    }

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