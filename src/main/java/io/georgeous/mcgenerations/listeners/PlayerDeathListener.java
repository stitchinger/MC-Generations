package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.ServerConfig;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.BlockFacing;
import io.georgeous.mcgenerations.utils.ItemManager;
import org.bukkit.ChatColor;
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

public class PlayerDeathListener implements Listener {
    private final RoleManager roleManager = RoleManager.getInstance();
    PlayerManager playerManager = PlayerManager.getInstance();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        dealWithRoleDeath(event);
        removeMember(player);
        SurrogateManager.getInstance().destroyPlayer(event.getEntity());
    }

    private void removeMember(Player player) {
        PlayerRole role = RoleManager.getInstance().get(player);
        if (role == null) {
            return;
        }
        role.getFamily().removeMember(role);
    }

    private void dealWithRoleDeath(PlayerDeathEvent event){
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
        player.setBedSpawnLocation(ServerConfig.getInstance().getCouncilLocation(), true);

        if(!PlayerManager.getInstance().getWrapper(player).isDebugMode())
            createGrave(playerRole);

        PlayerManager.getInstance().getWrapper(player).addLife();
        playerRole.die();
    }

    private void createGrave(PlayerRole role){
        // Place Grave Sign
        World world = role.getPlayer().getWorld();

        // Find Ground
        Block under = world.getBlockAt(role.getPlayer().getLocation());
        do{
           under = under.getRelative(BlockFace.DOWN);
        }while(under.getType() == Material.AIR);

        Block block1 = under.getRelative(BlockFace.UP);

        // Random Sign Type
        Material[] signTypes = {Material.OAK_SIGN, Material.BIRCH_SIGN, Material.SPRUCE_SIGN, Material.ACACIA_SIGN, Material.JUNGLE_SIGN};
        int i = (int) (Math.random() * (signTypes.length - 1));
        Material signType = signTypes[i];
        block1.setType(signType);

        // Set rotation to players rotation
        Rotatable bd = ((Rotatable) block1.getBlockData());
        BlockFace face = BlockFacing.locationToFace(role.getPlayer().getLocation());
        bd.setRotation(face);
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

    public void removeBabyHandlerFromDrops(PlayerDeathEvent event) {
        event.getDrops().stream().filter(ItemManager::isBabyHandler).toList().forEach(item -> item.setAmount(0));
    }


}
