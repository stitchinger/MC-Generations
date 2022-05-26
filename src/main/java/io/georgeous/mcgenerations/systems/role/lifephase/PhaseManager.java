package io.georgeous.mcgenerations.systems.role.lifephase;

import io.georgeous.mcgenerations.events.PlayerPhaseUpEvent;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.components.PlayerAge;
import io.georgeous.mcgenerations.systems.surrogate.SurrogateManager;
import io.georgeous.mcgenerations.utils.Notification;
import io.georgeous.mcgenerations.utils.Util;
import io.georgeous.piggyback.Piggyback;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import xyz.haoshoku.nick.api.NickAPI;

public class PhaseManager {
    PlayerRole playerRole;
    Player player;
    PlayerAge am;

    private Phase phase;

    public PhaseManager(PlayerRole playerRole, PlayerAge am) {
        this.playerRole = playerRole;
        this.player = playerRole.getPlayer();
        this.am = am;

        checkPhaseUp(am.getAge());
    }

    public void update() {
        checkPhaseUp(am.getAge());
        phaseUpdate();
        babyHungerScream();
    }

    private void babyHungerScream() {
        if (phase == Phase.BABY || phase == Phase.TODDLER) {
            if (player.getFoodLevel() < 10) {
                double freq = Util.map(
                        player.getFoodLevel(),
                        0,
                        10,
                        10,
                        1);
                if (Math.random() < freq / 100d) { // triggers every 5 secs in average
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GOAT_SCREAMING_AMBIENT, SoundCategory.MASTER, 1, 2);
                }
            }
        }
    }

    public Phase getCurrentPhase() {
        return phase;
    }

    // Life Phases
    public void checkPhaseUp(int age) {
        for (Phase phase : Phase.values()) {
            int start = phase.getStartAge();
            int end = phase.getEndAge();

            if (age >= start && age < end) { // in Age range
                if (this.phase != phase) {  // not already in this phase?
                    PlayerPhaseUpEvent e = new PlayerPhaseUpEvent(player, this, this.phase, phase);
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

    public void changePhase(Phase phase) {
        endPhase();

        // Stop carrying if one player phases up
        if (Piggyback.carryCoupleMap.containsKey(player)) {
            if (Piggyback.carryCoupleMap.carried.get(player) != null) {
                Player carrier = Piggyback.carryCoupleMap.carried.get(player).getCarrier();
                Piggyback.stopCarry(carrier);
                Notification.errorMsg(carrier, "Dog Sven vanished!");
                Notification.errorMsg(player, "Dog Sven vanished!");
            } else if (Piggyback.carryCoupleMap.carriers.get(player) != null) {
                Piggyback.stopCarry(player);
                Notification.errorMsg(player, "Dog Sven vanished!");
            }
        }

        this.phase = phase;
        start();

        phaseUpEffect();
    }

    public void phaseUpEffect() {
        Location location = player.getLocation();
        player.getWorld().spawnParticle(Particle.COMPOSTER, location, 100, 0.5, 1, 0.5);
        player.getWorld().playSound(location, Sound.BLOCK_BELL_USE, 4, 1);
        player.getWorld().playSound(location, Sound.BLOCK_BELL_RESONATE, 4, 1);
    }

    public void start() {
        Notification.neutralMsg(playerRole.getPlayer(), "You are a §a" + phase.name);

        Player player = playerRole.getPlayer();
        NickAPI.setSkin(player, phase.skin.value, phase.skin.signature);
        NickAPI.refreshPlayer(player);
        playerRole.refreshHealthBar();

        if (phase.surrogate) {
            SurrogateManager.getInstance().create(playerRole.getPlayer(), playerRole.getName() + " " + playerRole.family.getColoredName());
        }
    }

    public void endPhase() {
        if (phase == null)
            return;
        if (phase.surrogate) {
            SurrogateManager.getInstance().destroyPlayer(playerRole.getPlayer());
        }

        playerRole.getPlayer().removePotionEffect(PotionEffectType.SLOW);
        playerRole.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        playerRole.getPlayer().removePotionEffect(PotionEffectType.JUMP);
    }

    public void phaseUpdate() {
        phase.applyPotionEffects(playerRole.getPlayer(), phase.effects);

        if (Math.random() < phase.hungerRate / 100d) {
            int foodLevel = playerRole.getPlayer().getFoodLevel();
            if (foodLevel > 0) {
                playerRole.getPlayer().setFoodLevel(foodLevel - 1);
            }
        }
    }
}