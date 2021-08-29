package io.georgeous.mcgenerations.systems.role.lifephase.events;

import io.georgeous.mcgenerations.systems.role.lifephase.LifePhase;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerPhaseUpEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    Player player;
    PhaseManager phaseManager;
    LifePhase oldPhase;
    LifePhase newPhase;
    boolean canceled;

    public PlayerPhaseUpEvent(Player player, PhaseManager phaseManager, LifePhase oldPhase, LifePhase newPhase) {
        this.player = player;
        this.canceled = false;
        this.phaseManager = phaseManager;
        this.oldPhase = oldPhase;
        this.newPhase = newPhase;
    }

    public Player getPlayer() {
        return player;
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public LifePhase getOldPhase() {
        return oldPhase;
    }

    public LifePhase getNewPhase() {
        return newPhase;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }
}