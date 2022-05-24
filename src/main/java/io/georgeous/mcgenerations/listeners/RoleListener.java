package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.SpawnManager;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RoleListener implements Listener {

    private final RoleManager roleManager = RoleManager.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        roleManager.initPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        roleManager.saveRole(roleManager.get(player));
        roleManager.remove(player);
    }

    @EventHandler
    public void onRoleDead(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerRole playerRole = roleManager.get(player);
        if (playerRole == null) {
            return;
        }

        removeBabyHandlerFromDrops(event);

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

        event.setDeathMessage(msg);

        boolean diedOfOldAge = playerRole.getAgeManager().getAge() >= 60;
        if (diedOfOldAge) {
            event.setDeathMessage(roleName + " died of old Age. RIP");
            PlayerManager.getInstance().getWrapper(player).setDiedOfOldAge(true);
            PlayerManager.getInstance().getWrapper(player).setLastBedLocation(player.getBedSpawnLocation());
        }
        player.setBedSpawnLocation(MCG.council.councilLocation, true);


        createGrave(playerRole);
        playerRole.die();
    }

    private void createGrave(PlayerRole role){
        // Place Grave Sign
        World world = role.getPlayer().getWorld();
        Block block1 = world.getBlockAt(role.getPlayer().getLocation());

        // Random Sign Type
        Material[] signTypes = {Material.OAK_SIGN, Material.BIRCH_SIGN, Material.SPRUCE_SIGN, Material.ACACIA_SIGN, Material.JUNGLE_SIGN};
        int i = (int) (Math.random() * (signTypes.length - 1));
        Material signType = signTypes[i];
        block1.setType(signType);

        // Set rotation to players rotation
        Rotatable bd = ((Rotatable) block1.getBlockData());
        bd.setRotation(role.getPlayer().getFacing());
        block1.setBlockData(bd);

        // Random Grave Symbol
        String[] graveSymbols = {"☄", "♰", "☮", "☯", "Ω", "❤", "✿", "☪", "♬", "✟" };
        i = (int) (Math.random() * (graveSymbols.length - 1));
        String graveSymbol = graveSymbols[i];

        // Write on sign
        Sign sig = (Sign) block1.getState();
        sig.setLine(0, role.getName() + " " + role.getFamily().getName());
        sig.setLine(1, "Age: " + role.getAgeManager().getAge());
        sig.setLine(2, (MCG.serverYear - role.getAgeManager().getAge()) + " - " + MCG.serverYear);
        sig.setLine(3, "R.I.P.  " + graveSymbol);
        sig.update();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.getHealth() == 0)
            SpawnManager.spawnPlayer(player);
    }

    public void removeBabyHandlerFromDrops(PlayerDeathEvent event) {
        event.getDrops().stream().filter(ItemManager::isBabyHandler).toList().forEach(item -> item.setAmount(0));
    }
}