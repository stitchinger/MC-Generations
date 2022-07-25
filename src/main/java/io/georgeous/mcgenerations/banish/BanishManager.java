package io.georgeous.mcgenerations.banish;

import io.georgeous.mcgenerations.MCG;

import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Logger;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class BanishManager {

    private static BanishManager instance;
    private static final HashMap<UUID, BanishRequest> requests = new HashMap<>();
    private static final int UPDATE_TIME = 60;

    private BanishManager(){
        new BukkitRunnable(){
            @Override
            public void run() {
                Iterator<Map.Entry<UUID, BanishRequest>> iterator = requests.entrySet().iterator();
                while (iterator.hasNext()) {
                    BanishRequest request = iterator.next().getValue();

                    if(request.isTimeout()){
                        Logger.log("removed banish request from memory");
                        iterator.remove();
                    }
                }
            }
        }.runTaskTimer(MCG.getInstance(), 20L * UPDATE_TIME, 20L * UPDATE_TIME);
    }

    public static BanishManager get() {
        if (instance == null) {
            instance = new BanishManager();
        }
        return instance;
    }


    public void requestBanish(Player banisher, Player banished){
        if(banished == null){
            Notification.errorMsg(banisher, "No player found with this name");
            return;
        }

        PlayerRole banisherRole = RoleManager.get().get(banisher);
        PlayerRole banishedRole = RoleManager.get().get(banished);


        if(banisherRole.getFamily() != banishedRole.getFamily()){
            Notification.errorMsg(banisher, banishedRole.getName() + " is not in your family");
            return;
        }


        UUID uuid = UUID.randomUUID();
        BanishRequest request = new BanishRequest(banisherRole, banishedRole, uuid);
        requests.put(uuid, request);
    }

    public void accept(UUID uuid, Player player){
        BanishRequest request = requests.get(uuid);
        if(request == null){
            Notification.errorMsg(player, "Request invalid");
            return;
        }

        if(validRequest(request, player)){
            request.accept(player);
        }
    }

    public void decline(UUID uuid, Player player){
        BanishRequest request = requests.get(uuid);
        if(request == null){
            Notification.errorMsg(player, "Request invalid");
            return;
        }

        if(validRequest(request, player)){
            request.decline(player);
        }
    }

    private boolean validRequest(BanishRequest request, Player player ){
        if(request.isTimeout()){
            Notification.errorMsg(player, "Requests are only valid for 1 minute");
            return false;
        }

        if(request.getVotes().contains(player)){
            Notification.errorMsg(player, "You already voted");
            return false;
        }

        return true;
    }
}
