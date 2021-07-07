package io.georgeous.mcgenerations.lifephase;

import io.georgeous.mcgenerations.player.AgeManager;
import io.georgeous.mcgenerations.player.PlayerRole;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PhaseManager {
    PlayerRole playerRole;
    Player p;
    AgeManager am;

    //private final LifePhase[] phases = new LifePhase[6];
    //private LifePhase currentPhase;

    private final LifePhaseNew[] phases = new LifePhaseNew[6];
    private LifePhaseNew currentPhase;


    public PhaseManager(PlayerRole playerRole, AgeManager am){
        this.playerRole = playerRole;
        this.p = playerRole.player;
        this.am = am;
        LifePhaseNew babyPhase = new LifePhaseNew(this.p,5,3,false,false,"12456", "Baby",128,6,2);
        LifePhaseNew toddlerPhase = new LifePhaseNew(this.p,2,6,false,false,"12456", "Toddler",128,6,2);
        LifePhaseNew childPhase = new LifePhaseNew(this.p,0,10,false,false,"12456", "Child",0,0,0);
        LifePhaseNew teenPhase = new LifePhaseNew(this.p,0,999,false,false,"12456", "Teen",0,0,0);
        LifePhaseNew adultPhase = new LifePhaseNew(this.p,0,999,false,false,"12456", "Adult",0,0,0);
        LifePhaseNew elderPhase = new LifePhaseNew(this.p,0,999,false,false,"12456", "Elder",0,0,0);

        phases[0] = babyPhase;
        phases[1] = toddlerPhase;
        phases[2] = childPhase;
        phases[3] = teenPhase;
        phases[4] = adultPhase;
        phases[5] = elderPhase;
        currentPhase = phases[0];

        /*
        phases[0] = new BabyPhase(this.p);
        phases[1] = new ToddlerPhase(this.p);
        phases[2] = new ChildPhase(this.p);
        phases[3] = new TeenPhase(this.p,this.playerRole.playerWrapper);
        phases[4] = new AdultPhase(this.p);
        phases[5] = new ElderPhase(this.p);
        currentPhase = phases[0];

         */
    }

    public void update(){
        checkPhaseUp();
        currentPhase.update();
    }

    public LifePhaseNew getCurrentPhase(){
        return currentPhase;
    }


    // Life Phases
    public void checkPhaseUp() {
        if (am.ageInYears < 3) {
            if (currentPhase != phases[LifePhase.BABY_PHASE]) {
                changePhase(LifePhase.BABY_PHASE);
            }
        }
        if (am.ageInYears >= 3 && am.ageInYears < 6) {
            if (currentPhase != phases[LifePhase.TODDLER_PHASE]) {
                changePhase(LifePhase.TODDLER_PHASE);
            }

        }
        if (am.ageInYears >= 6 && am.ageInYears < 15) {
            if (currentPhase != phases[LifePhase.CHILD_PHASE]) {
                changePhase(LifePhase.CHILD_PHASE);
            }
        }
        if (am.ageInYears >= 15 && am.ageInYears < 21) {
            if (currentPhase != phases[LifePhase.TEEN_PHASE]) {
               changePhase(LifePhase.TEEN_PHASE);
            }
        }
        if (am.ageInYears >= 21 && am.ageInYears < 40) {
            if (currentPhase != phases[LifePhase.ADULT_PHASE]) {
               changePhase(LifePhase.ADULT_PHASE);
            }
        }
        if (am.ageInYears >= 40 && am.ageInYears < 60) {
            if (currentPhase != phases[LifePhase.ELDER_PHASE]) {
               changePhase(LifePhase.ELDER_PHASE);
            }
        }

        if (am.ageInYears >= 60 && !playerRole.isDead) {
            p.sendMessage("You died of old age. RIP");
            playerRole.die();
        }
    }

    public void changePhase(int phaseIndex){
        if(currentPhase != null){
            currentPhase.end();
            currentPhase = null;
        }
        currentPhase = phases[phaseIndex];
        currentPhase.start();

        phaseUpEffect();
    }

    public void phaseUpEffect(){
        this.p.getWorld().spawnParticle(Particle.COMPOSTER,this.p.getLocation(),100,0.5,1,0.5);
        this.p.playSound(this.p.getLocation(), Sound.BLOCK_BELL_USE, 4, 1);
        this.p.playSound(this.p.getLocation(), Sound.BLOCK_BELL_RESONATE, 1, 1);
    }


}
