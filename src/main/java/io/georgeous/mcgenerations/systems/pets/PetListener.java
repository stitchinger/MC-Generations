package io.georgeous.mcgenerations.systems.pets;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityTameEvent;

public class PetListener implements Listener {

    private final PetManager petManager;

    public PetListener(PetManager petManager){
        this.petManager = petManager;
    }

    @EventHandler
    public void onTaming(EntityTameEvent event){
        LivingEntity pet = event.getEntity();
        AnimalTamer owner = event.getOwner();

        if(owner instanceof Player){
            petManager.addPet((Player) owner,pet);
        }
    }

    @EventHandler
    public void onBreeding(EntityBreedEvent event){
        LivingEntity newBornAnimal = event.getEntity();
        LivingEntity breeder = event.getBreeder();

        if(breeder instanceof Player){
            if(newBornAnimal instanceof Wolf || newBornAnimal instanceof Cat){
                petManager.addPet((Player) breeder, newBornAnimal);
            }
        }
    }
}
