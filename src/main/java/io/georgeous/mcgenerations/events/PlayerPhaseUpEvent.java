package io.georgeous.mcgenerations.events;

import io.georgeous.mcgenerations.systems.role.lifephase.Phase;
import io.georgeous.mcgenerations.systems.role.lifephase.PhaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerPhaseUpEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final PhaseManager phaseManager;
    private final Phase oldPhase;
    private final Phase newPhase;
    private boolean canceled;

    public PlayerPhaseUpEvent(Player player, PhaseManager phaseManager, Phase oldPhase, Phase newPhase) {
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

    public Phase getOldPhase() {
        return oldPhase;
    }

    public Phase getNewPhase() {
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