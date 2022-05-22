package io.georgeous.mcgenerations.systems.role;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class RoleListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        RoleManager.initPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        RoleManager.saveRole(RoleManager.get(player));
        RoleManager.remove(player);
    }

    @EventHandler
    public void onRoleDead(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerRole playerRole = RoleManager.get(player);
        if (playerRole == null) {
            return;
        }

        removeBabyHandlerFromDrops(event);

        String roleName = playerRole.getName() + " " + playerRole.getFamily().getColoredName() + ChatColor.RESET;
        String ageString = "(" + playerRole.am.getAge() + ")";
        String msg = event.getDeathMessage().replace(player.getName(), roleName + " " + ageString);

        // Replace killers real name with character name
        Player killer = player.getKiller();
        if (killer != null) {
            PlayerRole killerRole = RoleManager.get(killer);
            if (killerRole != null) {
                String killersCharName = killerRole.getName() + " " + killerRole.getFamily().getColoredName() + ChatColor.RESET;
                msg = msg.replace(killer.getName(), killersCharName);
            }
        }

        event.setDeathMessage(msg);

        boolean diedOfOldAge = playerRole.am.getAge() >= 60;
        if (diedOfOldAge) {
            event.setDeathMessage(roleName + " died of old Age. RIP");
            PlayerManager.get(player).setDiedOfOldAge(true);
            PlayerManager.get(player).setLastBedLocation(player.getBedSpawnLocation());
        }
        player.setBedSpawnLocation(MCG.council.councilLocation, true);

        playerRole.die();

        // Place Grave Sign
        World world = player.getWorld();
        player.getLocation();
        Block block1 = world.getBlockAt(player.getLocation());
        block1.setType(Material.OAK_SIGN);

        ((Rotatable) block1.getBlockData()).setRotation(BlockFace.EAST_NORTH_EAST);
        //rot.setRotation(player.getFacing());


        Sign sig = (Sign) block1.getState();




        sig.setLine(0, "R.I.P.");
        sig.setLine(1, playerRole.getName() + " " + playerRole.getFamily().getName());
        sig.setLine(2, 1989 + " - " + 2046);
        sig.update();

    }


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.getHealth() == 0)
            SpawnManager.spawnPlayer(player);
    }

    public void removeBabyHandlerFromDrops(PlayerDeathEvent event) {
        for (ItemStack item : event.getDrops()) {
            if (ItemManager.isBabyHandler(item)) {
                item.setAmount(0);
            }
        }
    }
}