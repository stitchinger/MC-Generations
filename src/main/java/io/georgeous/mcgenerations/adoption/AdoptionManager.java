package io.georgeous.mcgenerations.adoption;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.systems.role.lifephase.Phase;
import io.georgeous.mcgenerations.utils.Logger;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class AdoptionManager {

    private static AdoptionManager instance;
    private static final HashMap<UUID, AdoptionRequest> requests = new HashMap<>();
    private static final int UPDATE_TIME = 60;

    private AdoptionManager(){
        new BukkitRunnable(){
            @Override
            public void run() {
                Iterator<Map.Entry<UUID, AdoptionRequest>> iterator = requests.entrySet().iterator();
                while (iterator.hasNext()) {
                    AdoptionRequest request = iterator.next().getValue();

                    if(!request.isValid()){
                        Logger.log("removed adoption request from memory");
                        iterator.remove();
                    }
                }
            }
        }.runTaskTimer(MCG.getInstance(), 20L * UPDATE_TIME, 20L * UPDATE_TIME);
    }

    public static AdoptionManager get() {
        if (instance == null) {
            instance = new AdoptionManager();
        }
        return instance;
    }


    public void requestAdoptation(Player adopter, Player adoptee){
            if(adoptee == null){
                Notification.errorMsg(adopter, "No player found with this name");
                return;
            }

            PlayerRole adopterRole = RoleManager.get().get(adopter);
            PlayerRole adopteeRole = RoleManager.get().get(adoptee);

            Family adopterFamily = adopterRole.getFamily();
            Family adopteeFamily = adopteeRole.getFamily();

            if(adopterFamily == adopteeFamily){
                Notification.errorMsg(adopter, adopteeRole.getName() + " is already in your family");
                return;
            }

            if(adopterRole.getUsedAdopt()){
                Notification.errorMsg(adopter, "You can only adopt one player per life");
                return;
            }

            Phase minPhase = Phase.CHILD;
            Phase currentPhase = adopterRole.getPhaseManager().getCurrentPhase();
            if(currentPhase.getId() < minPhase.getId()){
                Notification.errorMsg(adopter, "You must be at least " + minPhase.getStartAge() + " years old to adopt another player");
                return;
            }

            currentPhase = adopteeRole.getPhaseManager().getCurrentPhase();
            if(currentPhase.getId() < minPhase.getId()){
                Notification.errorMsg(adopter, "Players must be at least " + minPhase.getStartAge() + " years old to adopt them");
                return;
            }

            UUID uuid = UUID.randomUUID();
            AdoptionRequest request = new AdoptionRequest(adopterRole, adopteeRole, uuid);
            requests.put(uuid, request);


    }

    public void accept(UUID uuid, Player player){
        AdoptionRequest request = requests.get(uuid);
        if(request == null){
            Notification.errorMsg(player, "Request invalid");
            return;
        }
        requests.remove(uuid);

        if(validRequest(request, player)){
            request.accept();
        }
    }

    public void decline(UUID uuid, Player player){
        AdoptionRequest request = requests.get(uuid);
        if(request == null){
            Notification.errorMsg(player, "Request invalid");
            return;
        }
        requests.remove(uuid);

        if(validRequest(request, player)){
            request.decline();
        }
    }

    private boolean validRequest(AdoptionRequest request, Player player ){
        if(!request.isValid()){
            Notification.errorMsg(player, "Requests are only valid for 1 minute");
            return false;
        }

        if(!request.adopter.getPlayer().isOnline()){
            Notification.errorMsg(player, request.adopter.getName() + " " + request.adopter.getFamily().getColoredName() + " left the game");
            return false;
        }

        if(request.adopter != RoleManager.get().get(request.adopter.getPlayer())){
            Notification.errorMsg(player, request.adopter.getName() + " " + request.adopter.getFamily().getColoredName() + " doesnt exist anymore");
            return false;
        }

        if(request.adoptee != RoleManager.get().get(request.adoptee.getPlayer())){
            Notification.errorMsg(player, request.adoptee.getName() + " " + request.adoptee.getFamily().getColoredName() + " doesnt exist anymore");
            return false;
        }

        return true;
    }
}
