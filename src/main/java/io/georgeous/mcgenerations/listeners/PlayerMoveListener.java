package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import javax.management.relation.Role;
import java.util.Random;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        councilSpawnTrigger(player, event.getTo());
        preventBabyBedBug(event);
    }

    private void preventBabyBedBug(PlayerMoveEvent event){
        Player player = event.getPlayer();

        PlayerRole playerRole = RoleManager.get().get(player);

        if(playerRole == null){
            return;
        }

        if(playerRole.getAgeManager().getAge() >= 3){
            return;
        }

        Material type = event.getFrom().getBlock().getType();

        if (isBed(type)) {
            Location target = event.getTo().clone();
            target.setY(event.getFrom().getY());
            event.setTo(target);
        }
    }

    private void councilSpawnTrigger(Player player, Location loc){
        float triggerRadius = 2;
        if(loc.getWorld() != MCG.overworld)
            return;

        if (loc.distance(McgConfig.getCouncilLocation()) < triggerRadius) {
            // Steps into light and start life
            PlayerWrapper playerWrapper = PlayerManager.get().getWrapper(player);
            if(playerWrapper != null){
                if(playerWrapper.getRulesAccepted()){
                    SpawnManager.get().spawnPlayer(player);
                } else{
                    Notification.errorMsg(player, "Before you can play you need to read and accept our rules. Use [/rules]");
                }

            }

        }
    }

    private boolean isBed(Material material) {
        Material[] beds = {
                Material.WHITE_BED, Material.BLACK_BED, Material.BLUE_BED, Material.RED_BED, Material.GREEN_BED, Material.YELLOW_BED,
                Material.BROWN_BED, Material.CYAN_BED, Material.MAGENTA_BED, Material.GRAY_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED,
                Material.LIME_BED, Material.ORANGE_BED, Material.PINK_BED, Material.PURPLE_BED
        };

        for (Material bed : beds) {
            if(material == bed){
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onCropJump(PlayerInteractEvent event){
        if(event.getAction() != Action.PHYSICAL){
            return;
        }

        if(event.getClickedBlock() == null){
            return;
        }

        if(event.getClickedBlock().getType() != Material.FARMLAND){
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void protectCouncil(PlayerMoveEvent event){
        PlayerRole role = RoleManager.get().get(event.getPlayer());
        if(role == null){
            return;
        }

        Location loc = event.getTo();

        if(withinProtection(loc)){
            lightningStrike(loc);
        }else if(withinWarningArea(loc)){
            lightningStrikeWarning(loc);
        }
    }

    private boolean withinWarningArea(Location loc){
        return withinCylinder(McgConfig.getCouncilLocation(), McgConfig.getCouncilProtectionRadius() + McgConfig.getCouncilProtectionWarningDistance() , McgConfig.getCouncilProtectionHeight(),loc);
    }

    private boolean withinProtection(Location loc){
        return withinCylinder(McgConfig.getCouncilLocation(), McgConfig.getCouncilProtectionRadius(), McgConfig.getCouncilProtectionHeight(),loc);
    }

    private boolean withinCylinder(Location center, double radius, double minY, Location loc){
        if(center.getWorld() != loc.getWorld())
            return false;

        if(loc.getY() < minY){
            return false;
        }
        center.setY(loc.getY());

        if(loc.distance(center) > radius){
            return false;
        }
        return  true;
    }

    private void lightningStrike(Location loc){
        double random = Math.random();
        if(random < 0.01){
            MCG.overworld.spawnEntity(loc, EntityType.LIGHTNING);
        }
    }

    private void lightningStrikeWarning(Location loc){
        double random = Math.random();
        if(random < 0.02){
            double x = Math.random() - 0.5d;
            double z = Math.random() - 0.5d;
            Vector v = new Vector(x, 0, z);
            double m = 10;
            MCG.overworld.spawnEntity(loc.clone().add(v.normalize().multiply(10)), EntityType.LIGHTNING);
        }
    }


}
