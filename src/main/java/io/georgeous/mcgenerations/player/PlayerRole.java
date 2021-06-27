package io.georgeous.mcgenerations.player;


import io.georgeous.mcgenerations.Family;
import io.georgeous.mcgenerations.gadgets.PetManager;
import io.georgeous.mcgenerations.lifephase.PhaseManager;
import io.georgeous.mcgenerations.utils.ItemManager;
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
    public String firstName;
    private boolean isNamed;
    public boolean isDead = false;


    // Family
    public Family family;
    public int generation;

    public Player child = null;
    public List<Player> children;
    // Managers
    public AgeManager am;
    public PhaseManager pm;

    public PlayerRole(Player player) {
        this.player = player;
        this.playerWrapper = PlayerManager.get(player);
        this.generation = 1;
        setIdentity();

        am = new AgeManager(playerWrapper);
        pm = new PhaseManager(this, am);
        //NameManager.name(this.player, this.firstName, family.getName());
    }

    // Update Functions
    public void update() {
        if (!isDead) {
            childUpdate();
            am.update();
            pm.update();
        }
    }

    public void setIdentity(){
        this.firstName = NameGenerator.randomName(NameGenerator.firstNames);
        this.family = new Family(NameGenerator.randomName(NameGenerator.lastNames));
        isNamed = false;
        player.sendMessage("Your family: " + family.getName());
    }

    public void childUpdate() {
        // Shows childs foodlevel on baby-handler
        if (this.child != null) { // has child?
            if (this.child.getHealth() <= 0) {
                this.child = null;
            }
            float damage;
            damage = Util.map((float) this.child.getFoodLevel(), 0, 20f, 25, 0);
            PlayerInventory inventory = player.getInventory();
            ItemStack[] stack = inventory.getContents();
            updateBabyHandlerDamage(stack, damage);
        }
    }

    public void updateBabyHandlerDamage(ItemStack[] stack, float damage) {
        for (ItemStack item : stack) {
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getDisplayName().contains("Baby-Handler")) {
                    ((Damageable) meta).setDamage((int) damage);
                    item.setItemMeta(meta);
                }
            }
        }
    }

    // Naming
    public void setName(String n) {
        this.firstName = n;
    }

    public String getName(){
        return firstName;
    }

    public void setNamed(boolean value){
        isNamed = value;
    }

    public boolean isNamed(){
        return isNamed;
    }

    // Dying
    public void die() {
        if (!isDead) {
            this.isDead = true;
            passOnPets();
            if (player.getHealth() != 0) {
                player.setHealth(0);
            }
        }
    }

    public void passOnPets(){
        if(getOldestChild() != null){
            PetManager.passPets(this.player, getOldestChild());
        } else {
            PetManager.releasePets(this.player);
        }
    }

    public Player getOldestChild() {
        if(children == null)
            return null;

        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) != null) {
                return children.get(i);
            }
        }
        return null;
    }

    // Abstrahieren? Gib kein Item, wenn schon vorhanden fadkljflkadjflak
    public void receiveBabyHandler() {
        if (Util.findInInventory("Baby-Handler", player.getInventory()) == null) {
            player.getInventory().addItem(ItemManager.getBabyHandler());
        }
    }

}
