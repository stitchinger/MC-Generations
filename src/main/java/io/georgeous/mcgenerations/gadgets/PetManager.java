package io.georgeous.mcgenerations.gadgets;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
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

        if(owner instanceof Player){
            addPet((Player) owner,pet);
        }
    }

    @EventHandler
    public void onBreeding(EntityBreedEvent event){
        LivingEntity newBornAnimal = event.getEntity();
        LivingEntity breeder = event.getBreeder();

        if(breeder instanceof Player){
            if(newBornAnimal instanceof Wolf || newBornAnimal instanceof Cat){
                addPet((Player) breeder, newBornAnimal);
            }
        }
    }

    public static void addPet(Player p, LivingEntity pet){
        if(ownerPets.get(p) == null){
            ownerPets.put(p, new ArrayList<>());
        }
        ownerPets.get(p).add(pet);
        p.sendMessage("Added pet to this player.");
        p.sendMessage("Pets: " + ownerPets.get(p).size());
    }

    public static void removePet(Player player, LivingEntity pet){
        if(ownerPets.get(player) == null){
            return;
        }
        ownerPets.get(player).remove(pet);
    }

    public static void releasePet(Entity entity){
        if(entity instanceof Sittable){
            ((Sittable) entity).setSitting(false);
        }
        if(entity instanceof Tameable){
            ((Tameable) entity).setTamed(false);
            ((Tameable) entity).setOwner(null);
        }
    }

    public static void releaseAllPets(Player player){
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
            LivingEntity entity = iterator.next();

            if(entity instanceof Tameable){
                //ownerPets.get(owner).remove(entity);
                ((Tameable) entity).setOwner(receiver);
                addPet(receiver, entity);
                iterator.remove();
            }
        }
    }
}
