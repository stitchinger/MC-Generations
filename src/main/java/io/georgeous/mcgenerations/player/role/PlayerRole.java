package io.georgeous.mcgenerations.player.role;


import io.georgeous.mcgenerations.family.Family;
import io.georgeous.mcgenerations.family.FamilyManager;
import io.georgeous.mcgenerations.gadgets.PetManager;
import io.georgeous.mcgenerations.player.role.components.AgeManager;
import io.georgeous.mcgenerations.player.role.components.MotherController;
import io.georgeous.mcgenerations.player.role.lifephase.PhaseManager;
import io.georgeous.mcgenerations.manager.SurroManager;
import io.georgeous.mcgenerations.player.PlayerManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import io.georgeous.mcgenerations.utils.NameGenerator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayerRole {
    // todo Ablaufdatum
    // Wenn player länger als 5min offline, töte Role
    // Player
    private final Player player;
    private String name;
    private boolean namedByMother;
    public boolean isDead = false;

    // Family
    public Family family;
    public int generation;

    // Managers
    public AgeManager am;
    public PhaseManager pm;
    public MotherController mc;

    public PlayerRole(Player player, String name, int age, Family family) {
        this.player = player;
        this.generation = 1;

        this.name = name;
        this.family = family;

        am = new AgeManager(this, age);
        pm = new PhaseManager(this, am);
        mc = new MotherController(this);
        //NameManager.name(this.player, this.firstName, family.getName());
        //player.sendMessage("You are " + this.getName() + " " + family.getName());
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

    public void setName(String name) {
        this.name = name;
        if(SurroManager.map.get(getPlayer()) != null){
            SurroManager.destroySurrogate(getPlayer());
            SurroManager.create(getPlayer(), name + " " + family.getName());
        }
    }

    public void rename(String name){
        if(namedByMother){
            System.out.println("Role already named by mother");
            return;
        }
        setName(name);
        namedByMother = true;
    }

    public boolean isNamedByMother() {
        return namedByMother;
    }


    public void setRandomIdentity() {
        //this.setName(NameGenerator.randomName(NameGenerator.firstNames));
        //name = NameGenerator.randomName(NameGenerator.firstNames);

        //isNamed = false;
    }

    public Family getFamily(){
        return family;
    }

    // Dying

    public void die() {
        if (!isDead) {
            this.isDead = true;
            passOnPetsToDescendent();
            if (getPlayer().getHealth() != 0) {
                getPlayer().setHealth(0);
            }
        }
    }

    public void restoreFrom(ConfigurationSection config){
        am.setAge(config.getInt("age"));
        name = config.getString("name");
        family.setName(config.getString("familyname"));
    }

    public void passOnPetsToDescendent() {
        if (mc.getOldestChild() != null) {
            PetManager.passPets(this.getPlayer(), mc.getOldestChild().getPlayer());
        } else {
            PetManager.releasePets(this.getPlayer());
        }
    }

    public Player getPlayer() {
        return player;
    }
}
