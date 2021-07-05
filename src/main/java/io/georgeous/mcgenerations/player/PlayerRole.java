package io.georgeous.mcgenerations.player;


import io.georgeous.mcgenerations.family.Family;
import io.georgeous.mcgenerations.family.FamilyManager;
import io.georgeous.mcgenerations.gadgets.PetManager;
import io.georgeous.mcgenerations.lifephase.PhaseManager;
import io.georgeous.mcgenerations.utils.NameGenerator;
import io.georgeous.mcgenerations.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlayerRole {
    // Player
    public final Player player;
    public PlayerWrapper playerWrapper;
    private String name;
    private boolean isNamed;
    public boolean isDead = false;

    // Family
    public Family family;
    public int generation;

    // Managers
    public AgeManager am;
    public PhaseManager pm;
    public MotherController mc;

    public PlayerRole(Player player) {
        this.player = player;
        this.playerWrapper = PlayerManager.get(player);
        this.generation = 1;
        setIdentity();

        am = new AgeManager(playerWrapper);
        pm = new PhaseManager(this, am);
        mc = new MotherController(this);
        //NameManager.name(this.player, this.firstName, family.getName());
    }

    // Update Functions
    public void update() {
        if (!isDead) {
            mc.update();
            am.update();
            pm.update();
        }
    }

    // Naming
    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public boolean isNamed() {
        return isNamed;
    }

    public void setNamed(boolean value) {
        isNamed = value;
    }

    public void setIdentity() {
        this.setName(NameGenerator.randomName(NameGenerator.firstNames));
        //this.family = new Family(NameGenerator.randomName(NameGenerator.lastNames));
        this.family = FamilyManager.addFamily(NameGenerator.randomName(NameGenerator.lastNames));
        //isNamed = false;
        player.sendMessage("You are " + this.getName() + " " + family.getName());
    }

    // Dying

    public void die() {
        if (!isDead) {
            this.isDead = true;
            passOnPetsToDescendent();
            if (player.getHealth() != 0) {
                player.setHealth(0);
            }
        }
    }

    public void passOnPetsToDescendent() {
        if (mc.getOldestChild() != null) {
            PetManager.passPets(this.player, mc.getOldestChild());
        } else {
            PetManager.releasePets(this.player);
        }
    }

    public void setItemsDamage(ItemStack item, float damage) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if(meta != null){
                if(meta.hasDisplayName()){
                    if (meta.getDisplayName().contains("Baby-Handler")) {
                        if(meta instanceof Damageable){
                            ((Damageable) meta).setDamage((int) damage);
                            item.setItemMeta(meta);
                        }

                    }
                }
            }
        }
    }


}
