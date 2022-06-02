package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.haoshoku.nick.api.NickAPI;

public class PlayerDeathListener implements Listener {
    private final RoleManager roleManager = RoleManager.get();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        removeBabyHandlerFromDrops(event);
        PlayerRole role = RoleManager.get().get(player);
        if(role == null){
            event.setDeathMessage("");
            return;
        }

        event.setDeathMessage(getRoleDeathMessage(event));
        RoleManager.get().removeRoleData(role);
        RoleManager.get().removeRoleOfPlayer(player);

        //dealWithRoleDeath(event);
        //removeMember(player);
        //SurrogateManager.getInstance().destroySurrogateOfPlayer(player);
    }

    @EventHandler
    public void onBeforePlayerDeath(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player player)){
            return;
        }
        if(player.getHealth() - event.getDamage() < 1){
            //player.teleport(ServerConfig.getInstance().getCouncilLocation());
            event.setCancelled(true);
            NickAPI.resetNick(player);
            NickAPI.resetSkin(player);
            NickAPI.resetUniqueId(player);
            NickAPI.resetGameProfileName(player);
            NickAPI.refreshPlayer(player);
            player.setHealth(0);
            new BukkitRunnable(){
                @Override
                public void run() {

                }
            }.runTaskLater(MCG.getInstance(), 20L * 2);
        }
    }

    private void removeMember(Player player) {
        PlayerRole role = RoleManager.get().get(player);
        if (role == null) {
            return;
        }
        role.getFamily().removeMember(role);

    }

    private String getRoleDeathMessage(PlayerDeathEvent event){
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
        return msg;
    }

    private void dealWithRoleDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        PlayerRole playerRole = roleManager.get(player);
        if (playerRole == null) {
            return;
        }

        String roleName = playerRole.getName() + " " + playerRole.getFamily().getColoredName() + ChatColor.RESET;

        String msg = getRoleDeathMessage(event);
        if(msg != null)
            event.setDeathMessage(msg);


        // Died of old Age?
        boolean diedOfOldAge = playerRole.getAgeManager().getAge() >= 60;
        if (diedOfOldAge) {
            event.setDeathMessage(roleName + " died of old Age. RIP");
            PlayerManager.get().getWrapper(player).setDiedOfOldAge(true);
            PlayerManager.get().getWrapper(player).setLastBedLocation(player.getBedSpawnLocation());
        }


        player.setBedSpawnLocation(McgConfig.getCouncilLocation(), true);

        if(!PlayerManager.get().getWrapper(player).isDebugMode() && playerRole.getAgeManager().getAge() >= 6) {
            createGrave(playerRole);
        }

        PlayerManager.get().getWrapper(player).addLife();
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
