package io.georgeous.mcgenerations.player;

import org.bukkit.entity.Player;


public class PlayerWrapper {
    public final Player player;
    private PlayerRole playerRole;

    public boolean canCarry = false;
    public boolean canBeCarried = false;

    public PlayerWrapper(Player player) {
        this.player = player;
        this.setRole(null);
    }

    // Update Functions
    public void update() {
        if(getRole() != null)
            getRole().update();
    }

    public PlayerRole getRole() {
        return playerRole;
    }

    public void setRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public void removeRole(){
        this.setRole(null);
    }
}
