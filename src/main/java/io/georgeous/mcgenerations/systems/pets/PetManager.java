package io.georgeous.mcgenerations.systems.pets;

import io.georgeous.mcgenerations.MCG;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class PetManager implements Listener {

    private HashMap<Player, ArrayList<LivingEntity>> ownerPets = new HashMap<>();
    private MCG plugin;


    public PetManager(MCG plugin){
        this.plugin = plugin;

        registerCommands();
        registerEvents();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(playerDataExists(player)) {
                restore(player);
            }
        }
    }

    private void registerCommands() {
        PetCommand petCommand = new PetCommand(this);
        plugin.getServer().getPluginCommand("pets").setExecutor(petCommand);
        plugin.getCommand("pets").setTabCompleter(petCommand);
    }

    private void registerEvents(){
        PetListener petListener = new PetListener(this);
        getServer().getPluginManager().registerEvents(petListener, plugin);
    }

    public void disable(){
        save();
    }

    public void save() {
        if (ownerPets.isEmpty())
            return;
        for (Map.Entry<Player, ArrayList<LivingEntity>> entry : ownerPets.entrySet()) { // Every Petowner
            if(entry.getValue() != null){
                FileConfiguration config = MCG.getInstance().getConfig();
                String uuid = entry.getKey().getUniqueId().toString();

                List<String> uuidList = new ArrayList<>();

                for(LivingEntity pet : entry.getValue()){ // Every pet
                    String petUuid = pet.getUniqueId().toString();
                    uuidList.add(petUuid);
                }

                config.set("data.pets." + uuid, uuidList);
                MCG.getInstance().saveConfig();
            }
        }

    }

    public void restore(Player player){
        String uuid = player.getUniqueId().toString();
        FileConfiguration config = MCG.getInstance().getConfig();
        //ConfigurationSection configSection = config.getConfigurationSection("data.pets." + uuid);


        List<String> petsUuid = config.getStringList("data.pets." + uuid);

        for(String id : petsUuid){
            addPet(player, (LivingEntity) Bukkit.getEntity(UUID.fromString(id)));
        }


        //config.set("data.player." + uuid + ".role", null);
        MCG.getInstance().saveConfig();
    }



    public void addPet(Player p, LivingEntity pet){
        if(ownerPets.get(p) == null){
            ownerPets.put(p, new ArrayList<>());
        }
        ownerPets.get(p).add(pet);
        p.sendMessage("Added pet to this player.");
        p.sendMessage("Pets: " + ownerPets.get(p).size());
    }

    public void removePet(Player player, LivingEntity pet){
        if(ownerPets.get(player) == null){
            return;
        }
        ownerPets.get(player).remove(pet);
    }

    public void releasePet(Entity entity){
        if(entity instanceof Sittable){
            ((Sittable) entity).setSitting(false);
        }
        if(entity instanceof Tameable){
            ((Tameable) entity).setTamed(false);
            ((Tameable) entity).setOwner(null);
        }
    }

    public void releaseAllPets(Player player){
        if(ownerPets.get(player) == null)
            return;

        int count = 0;

        for (Iterator<LivingEntity> iterator = ownerPets.get(player).iterator(); iterator.hasNext(); ) {
            LivingEntity e = iterator.next();

            if(e instanceof Tameable){
                if(e instanceof Sittable){
                    ((Sittable) e).setSitting(false);
                }
                ((Tameable) e).setTamed(false);
                ((Tameable) e).setOwner(null);

                count++;
                iterator.remove();
            }
        }
        player.sendMessage("You released " + count + " pets");
    }

    public void passPets(Player owner, Player receiver){

        if(ownerPets.get(owner) == null || ownerPets.get(owner).size() == 0){
            owner.sendMessage("You dont have any pets");
            return;
        }
        if(receiver == null){
            owner.sendMessage("There are no other players nearby");
            return;
        }
        for (Iterator<LivingEntity> iterator = ownerPets.get(owner).iterator(); iterator.hasNext(); ) {
            LivingEntity entity = iterator.next();

            if(entity instanceof Tameable){
                //ownerPets.get(owner).remove(entity);
                ((Tameable) entity).setOwner(receiver);
                addPet(receiver, entity);
                iterator.remove();
            }
        }
    }

    public int getPetCount(Player owner){
        if(ownerPets.get(owner) == null){
            return 0;
        }
        return ownerPets.get(owner).size();
    }

    public boolean playerDataExists(Player player){
        return MCG.getInstance().getConfig().contains("data.pets." + player.getUniqueId().toString());
    }
}
