package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.BlockFacing;
import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.Logger;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Rotatable;
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
            createGrave(playerRole);
        }

        PlayerManager.get().getWrapper(player).addLife();
        Logger.log(playerRole.getName() + " died offline");
        playerRole.die();
    }

    private void createGrave(PlayerRole role) {
        // Place Grave Sign
        World world = role.getPlayer().getWorld();

        // Find Ground
        Block under = world.getBlockAt(role.getPlayer().getLocation());
        do {
            under = under.getRelative(BlockFace.DOWN);
        } while (under.getType() == Material.AIR);

        Block block = under.getRelative(BlockFace.UP);
        block.setType(randomSignType());
        rotateSign(block, role.getPlayer().getLocation());
        writeOnGrave((Sign) block.getState(), role);
    }

    private Material randomSignType(){
        Material[] signTypes = {Material.OAK_SIGN, Material.BIRCH_SIGN, Material.SPRUCE_SIGN, Material.ACACIA_SIGN, Material.JUNGLE_SIGN};
        int i = (int) (Math.random() * (signTypes.length - 1));
        return signTypes[i];
    }

    private String randomGraveSymbol(){
        String[] graveSymbols = {"☄", "♰", "☮", "☯", "Ω", "❤", "✿", "☪", "♬", "✟"};
        int i = (int) (Math.random() * (graveSymbols.length - 1));
        return graveSymbols[i];
    }

    private void rotateSign(Block block, Location location){
        Rotatable bd = ((Rotatable) block.getBlockData());
        BlockFace face = BlockFacing.locationToFace(location);
        bd.setRotation(face);
        block.setBlockData(bd);
    }

    private void writeOnGrave(Sign sign, PlayerRole role){
        sign.setLine(0, role.getName() + " " + role.getFamily().getName());
        sign.setLine(1, "Age: " + role.getAgeManager().getAge());
        sign.setLine(2, (MCG.serverYear - role.getAgeManager().getAge()) + " - " + MCG.serverYear);
        sign.setLine(3, "R.I.P.  " + randomGraveSymbol());
        sign.update();
    }

    private void removeBabyHandlerFromDrops(PlayerDeathEvent event) {
        event.getDrops().stream().filter(ItemManager::isBabyHandler).toList().forEach(item -> item.setAmount(0));
    }

    private void removeStarterItemsFromDrops(PlayerDeathEvent event) {
        event.getDrops().stream().filter(ItemManager::isStarterItem).toList().forEach(item -> item.setAmount(0));
    }
}
