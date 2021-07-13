package io.georgeous.mcgenerations.player.role.lifephase;

import io.georgeous.mcgenerations.player.role.components.AgeManager;
import io.georgeous.mcgenerations.player.role.PlayerRole;
import io.georgeous.mcgenerations.player.role.lifephase.events.PlayerPhaseUpEvent;
import io.georgeous.mcgenerations.skins.SkinManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class PhaseManager {
    PlayerRole playerRole;
    Player player;
    AgeManager am;

    private final LifePhase[] phases = new LifePhase[6];
    private LifePhase currentPhase;


    public PhaseManager(PlayerRole playerRole, AgeManager am){
        this.playerRole = playerRole;
        this.player = playerRole.getPlayer();
        this.am = am;

        LifePhase babyPhase = new LifePhase(playerRole,0,3,1,3,false,true,"", "Baby",128,6,2, true, SkinManager.BABY);
        LifePhase toddlerPhase = new LifePhase(playerRole,3,6,2,6,false,true,"", "Toddler",200,2,1, true, SkinManager.TODDLER);
        LifePhase childPhase = new LifePhase(playerRole,6,15,0,10,false,false,"2007359867", "Child",0,1,0, false, SkinManager.CHILD);
        LifePhase teenPhase = new LifePhase(playerRole,15,21,0,999,true,false,"297371", "Teen",0,0,0, false, SkinManager.TEEN);
        LifePhase adultPhase = new LifePhase(playerRole,21,40,0,999,true,false,"584227931", "Adult",0,0,0, false, SkinManager.ADULT);
        LifePhase elderPhase = new LifePhase(playerRole,40,60,0,999,true,false,"1144027445", "Elder",0,0,0, false, SkinManager.ELDER);

        phases[0] = babyPhase;
        phases[1] = toddlerPhase;
        phases[2] = childPhase;
        phases[3] = teenPhase;
        phases[4] = adultPhase;
        phases[5] = elderPhase;

        checkPhaseUp(am.getAge());
        //currentPhase = phases[0];

        //currentPhase.start();
    }

    public void update(){
        checkPhaseUp(am.getAge());
        currentPhase.update();
    }

    public LifePhase getCurrentPhase(){
        return currentPhase;
    }

    // Life Phases
    public void checkPhaseUp(int age) {
        for (LifePhase phase : phases) {
            int start = phase.getStartAge();
            int end = phase.getEndAge();

            if (age >= start && age < end) { // in Age range
                if (currentPhase != phase) {  // not already in this phase?
                    PlayerPhaseUpEvent e = new PlayerPhaseUpEvent(player, this, currentPhase, phase);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    if (!e.isCancelled()) {
                        changePhase(phase);
                    }
                }
                return;
            }

        }
        playerRole.die();
    }

    public void changePhase(LifePhase phase){
        if(currentPhase != null){
            currentPhase.end();
            currentPhase = null;
        }
        currentPhase = phase;
        currentPhase.start();

        phaseUpEffect();
    }

    public void phaseUpEffect(){
        Location location = player.getLocation();
        player.getWorld().spawnParticle(Particle.COMPOSTER, location,100,0.5,1,0.5);
        player.playSound(location, Sound.BLOCK_BELL_USE, 4, 1);
        player.playSound(location, Sound.BLOCK_BELL_RESONATE, 1, 1);
    }
}
