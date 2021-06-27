package io.georgeous.mcgenerations.gadgets;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PetManager implements Listener {

    public static HashMap<Player, ArrayList<LivingEntity>> ownerPets = new HashMap<>();

    @EventHandler
    public void onTaming(EntityTameEvent event){
        LivingEntity pet = event.getEntity();
        AnimalTamer owner = event.getOwner();
        Bukkit.getServer().getLogger().info("TAMEEVENT!!!!!!!!!!!!!!!!!");

        if(owner instanceof Player){
            Player p = (Player) owner;
            if(ownerPets.get(p) == null){
                ownerPets.put(p, new ArrayList<>());
            }
            ownerPets.get(p).add(pet);
            p.sendMessage("Added pet to this player.");
            p.sendMessage("Pets: " + ownerPets.get(p).size());
        }
    }

    public static void releasePets(Player player){
        if(ownerPets.get(player) == null)
            return;
        int count = 0;
        for (Iterator<LivingEntity> iterator = ownerPets.get(player).iterator(); iterator.hasNext(); ) {
            LivingEntity e = iterator.next();
            if(e instanceof Tameable){
                if(e instanceof Wolf){
                    ((Wolf) e).setSitting(false);
                }
                if(e instanceof Cat){
                    ((Cat) e).setSitting(false);
                }
                if(e instanceof Parrot){
                    ((Parrot) e).setSitting(false);
                }
                ((Tameable) e).setTamed(false);
                ((Tameable) e).setOwner(null);
                //ownerPets.get(player).remove(e);
                count++;
                iterator.remove();
            }
        }

        player.sendMessage("You released " + count + " pets");


    }

    public static void passPets(Player owner, Player receiver){

        if(ownerPets.get(owner) == null || ownerPets.get(owner).size() == 0){
            owner.sendMessage("You dont have any pets");
            return;
        }
        if(receiver == null){
            owner.sendMessage("There are no other players nearby");
            return;
        }
        for (Iterator<LivingEntity> iterator = ownerPets.get(owner).iterator(); iterator.hasNext(); ) {
            LivingEntity e = iterator.next();
            if(e instanceof Tameable){
                ((Tameable) e).setOwner(receiver);
                ownerPets.get(owner).remove(e);
            }
        }
    }
}
