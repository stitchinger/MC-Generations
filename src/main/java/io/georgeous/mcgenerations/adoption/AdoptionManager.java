package io.georgeous.mcgenerations.adoption;

import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class AdoptionManager {

    private static AdoptionManager instance;
    private static final HashMap<UUID, AdoptionRequest> requests = new HashMap<>();

    private AdoptionManager(){}

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
        request.accept();
        requests.remove(uuid);
    }

    public void decline(UUID uuid, Player player){
        AdoptionRequest request = requests.get(uuid);
        if(request == null){
            Notification.errorMsg(player, "Request invalid");
            return;
        }
        request.decline();
        requests.remove(uuid);
    }
}
