package io.georgeous.mcgenerations.utils.graves;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.BlockFacing;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Rotatable;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.UUID;

public class GraveManager {

    private static PriorityQueue<String> queue = new PriorityQueue<>();


    private static final HashMap<Location, PlayerRole> roles = new HashMap<>();




    public static void createGrave(PlayerRole role) {
        queue.offer("Test");


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

    private static Material randomSignType(){
        Material[] signTypes = {Material.OAK_SIGN, Material.BIRCH_SIGN, Material.SPRUCE_SIGN, Material.ACACIA_SIGN, Material.JUNGLE_SIGN};
        int i = (int) (Math.random() * (signTypes.length - 1));
        return signTypes[i];
    }

    private static String randomGraveSymbol(){
        String[] graveSymbols = {"☄", "♰", "☮", "☯", "Ω", "❤", "✿", "☪", "♬", "✟"};
        int i = (int) (Math.random() * (graveSymbols.length - 1));
        return graveSymbols[i];
    }

    private static void rotateSign(Block block, Location location){
        Rotatable bd = ((Rotatable) block.getBlockData());
        BlockFace face = BlockFacing.locationToFace(location);
        bd.setRotation(face);
        block.setBlockData(bd);
    }

    private static void writeOnGrave(Sign sign, PlayerRole role){
        sign.setLine(0, role.getName() + " " + role.getFamily().getName());
        sign.setLine(1, "Age: " + role.getAgeManager().getAge());
        sign.setLine(2, (MCG.serverYear - role.getAgeManager().getAge()) + " - " + MCG.serverYear);
        sign.setLine(3, "R.I.P.  " + randomGraveSymbol());
        sign.update();
    }

}
