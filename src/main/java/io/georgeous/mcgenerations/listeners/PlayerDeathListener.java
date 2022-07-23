package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.*;
import io.georgeous.mcgenerations.utils.graves.GraveManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;



public class PlayerDeathListener implements Listener {
    private final RoleManager roleManager = RoleManager.get();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        removeBabyHandlerFromDrops(event);
        removeStarterItemsFromDrops(event);

        PlayerRole role = RoleManager.get().get(player);
        if (role == null) {
            event.setDeathMessage("");
            return;
        }

        // Broadcast death msg
        String msg = getRoleDeathMessage(event);
        rangedBroadcast(player, msg, McgConfig.getChatRange());
        event.setDeathMessage("");

        role.getFamily().removeMember(role);

        dealWithRoleDeath(event);
        RoleManager.get().removeRoleData(role);
        RoleManager.get().removeRoleOfPlayer(player);
    }

    private void rangedBroadcast(Player sender, String msg, double range) {
        for (Player receivingPlayer : Bukkit.getOnlinePlayers()) {

            boolean sameWorld = receivingPlayer.getLocation().getWorld() == sender.getLocation().getWorld();
            if(!sameWorld) {
                continue;
            }
            double distanceBetweenPlayers = receivingPlayer.getLocation().distance(sender.getLocation());
            if(distanceBetweenPlayers > range){
                continue;
            }
            receivingPlayer.sendMessage( msg);
        }
    }

    private String getRoleDeathMessage(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerRole playerRole = roleManager.get(player);
        if (playerRole == null) {
            return null;
        }

        // Replace Names in Message
        String roleName = playerRole.getName() + " " + playerRole.getFamily().getColoredName() + ChatColor.RESET;
        String ageString = "(" + playerRole.getAgeManager().getAge() + ")";
        String msg = event.getDeathMessage();
        if (msg == null)
            msg = "";
        msg = msg.replace(player.getName(), roleName + " " + ageString);

        // Replace killers real name with character name
        Player killer = player.getKiller();
        if (killer != null) {
            PlayerRole killerRole = roleManager.get(killer);
            if (killerRole != null) {
                String killersCharName = killerRole.getName() + " " + killerRole.getFamily().getColoredName() + ChatColor.RESET;
                msg = msg.replace(killer.getName(), killersCharName);
            }
        }

        // Died of old Age?
        boolean diedOfOldAge = playerRole.getAgeManager().getAge() >= 60;
        if (diedOfOldAge) {
            PlayerWrapper p = PlayerManager.get().getWrapper(player);
            p.setDiedOfOldAge(true);
            p.setLastBedLocation(player.getBedSpawnLocation());
            p.setLastFamily(playerRole.getFamily());
            msg = roleName + " died of old Age. RIP";
           oldPeopleLoot(event);
        }

        return msg;
    }

    private void oldPeopleLoot(PlayerDeathEvent event){
        ItemStack[] loots = {
                new ItemStack(Material.DIAMOND, 1),
                new ItemStack(Material.DIAMOND, 2),
                new ItemStack(Material.DIAMOND, 3),
                new ItemStack(Material.GOLD_INGOT, 5),
                new ItemStack(Material.IRON_INGOT, 5)
        };
        int rand = Util.getRandomInt(loots.length);
        event.getDrops().add(loots[rand]);
    }

    private void dealWithRoleDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerRole playerRole = roleManager.get(player);
        if (playerRole == null) {
            return;
        }

        // Create Grave
        if (!PlayerManager.get().getWrapper(player).isDebugMode() && playerRole.getAgeManager().getAge() >= 40) {
            GraveManager.createGrave(playerRole);
        }

        PlayerManager.get().getWrapper(player).addLife();
        Logger.log(playerRole.getName() + " died offline");
        playerRole.die();
    }

    private void removeBabyHandlerFromDrops(PlayerDeathEvent event) {
        event.getDrops().stream().filter(ItemManager::isBabyHandler).toList().forEach(item -> item.setAmount(0));
    }

    private void removeStarterItemsFromDrops(PlayerDeathEvent event) {
        event.getDrops().stream().filter(ItemManager::isStarterItem).toList().forEach(item -> item.setAmount(0));
    }
}
