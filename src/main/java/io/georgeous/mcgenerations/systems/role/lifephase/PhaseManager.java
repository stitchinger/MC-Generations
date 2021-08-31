package io.georgeous.mcgenerations.systems.role.lifephase;

import io.georgeous.mcgenerations.systems.role.components.AgeManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.lifephase.events.PlayerPhaseUpEvent;
import io.georgeous.mcgenerations.utils.Skin;
import io.georgeous.piggyback.Piggyback;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class PhaseManager {
    PlayerRole playerRole;
    Player player;
    AgeManager am;

    private final LifePhase[] phases = new LifePhase[6];
    private LifePhase currentPhase;

    public PhaseManager(PlayerRole playerRole, AgeManager am) {
        this.playerRole = playerRole;
        this.player = playerRole.getPlayer();
        this.am = am;

        LifePhase babyPhase = new LifePhase(playerRole, 0, 3, 1, 999, false, true, "", "Baby", 128, 6, 2, true, Skin.BABY, true);
        LifePhase toddlerPhase = new LifePhase(playerRole, 3, 6, 1, 999, false, true, "", "Toddler", 200, 2, 1, true, Skin.TODDLER, true);
        LifePhase childPhase = new LifePhase(playerRole, 6, 15, 0, 999, false, false, "2007359867", "Child", 0, 1, 0, false, Skin.CHILD, true);
        LifePhase teenPhase = new LifePhase(playerRole, 15, 21, 0, 999, true, false, "297371", "Teen", 0, 0, 0, false, Skin.TEEN, false);
        LifePhase adultPhase = new LifePhase(playerRole, 21, 40, 0, 999, true, false, "584227931", "Adult", 0, 0, 0, false, Skin.ADULT, false);
        LifePhase elderPhase = new LifePhase(playerRole, 40, 9999999, 0, 999, true, false, "1144027445", "Elder", 0, 0, 0, false, Skin.ELDER, false);

        phases[0] = babyPhase;
        phases[1] = toddlerPhase;
        phases[2] = childPhase;
        phases[3] = teenPhase;
        phases[4] = adultPhase;
        phases[5] = elderPhase;

        checkPhaseUp(am.getAge());
    }

    public void update() {
        checkPhaseUp(am.getAge());
        currentPhase.update();
        // Baby screams when hungry
        if(currentPhase.getName().equalsIgnoreCase("baby")){
            if(player.getFoodLevel() <= 6){
                if (Math.random() < 1d / 100d) {
                    player.getWorld().playSound(player.getLocation(),Sound.ENTITY_GOAT_SCREAMING_AMBIENT, SoundCategory.MASTER,2,1);
                }
            }
        }
    }

    public LifePhase getCurrentPhase() {
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
        player.setHealth(0);
    }

    public void changePhase(LifePhase phase) {
        if (currentPhase != null) {
            currentPhase.end();
            currentPhase = null;
        }

        // Cancel carrying
        // todo extract to Piggyback?
        if (Piggyback.carryCoupleMap.containsKey(player)) {
            if (Piggyback.carryCoupleMap.carried.get(player) != null) {
                Player carrier = Piggyback.carryCoupleMap.carried.get(player).getCarrier();
                Piggyback.stopCarry(carrier);
            } else if (Piggyback.carryCoupleMap.carriers.get(player) != null) {
                Piggyback.stopCarry(player);
            }
        }


        currentPhase = phase;
        currentPhase.start();

        phaseUpEffect();
    }

    public void phaseUpEffect() {
        Location location = player.getLocation();
        player.getWorld().spawnParticle(Particle.COMPOSTER, location, 100, 0.5, 1, 0.5);
        player.getWorld().playSound(location, Sound.BLOCK_BELL_USE, 4, 1);
        player.getWorld().playSound(location, Sound.BLOCK_BELL_RESONATE, 4, 1);
    }
}