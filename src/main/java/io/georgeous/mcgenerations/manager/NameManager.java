package io.georgeous.mcgenerations.manager;


import io.georgeous.mcgenerations.Main;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.role.RoleManager;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class NameManager {
/*
    public static HashMap<UUID, NickManager> map = new HashMap<>();

    public static void name(Player player, String first, String last) {
        UUID uuid = player.getUniqueId();
        PlayerRole playerRole = RoleManager.get(player);


        if (map.get(uuid) == null) {
            map.put(uuid, new NickManager(player));
        }

        NickManager nm = map.get(uuid);


        if (nm.isNicked()) {
            player.sendMessage("Nicked already");
            nm.unnickPlayer();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                nm.nickPlayer(first + " " + last);
                nm.setPlayerListName(first + " " + last);
                nm.changeSkinToMineSkinId(playerRole.pm.getCurrentPhase().getSkinID());
            }
        }.runTaskLater(Main.getPlugin(), 50);



        player.sendMessage("Set new name in NameManager");
    }

    public static void changeSkin(Player player, String skinID) {

        UUID uuid = player.getUniqueId();
        if (map.get(uuid) == null) {
            map.put(uuid, new NickManager(player));
        }
        NickManager nm = map.get(uuid);
        if (nm.isNicked()){
            //nm.unnickPlayer();
        }
        map.get(uuid).changeSkinToMineSkinId(skinID);


    }

 */
}
