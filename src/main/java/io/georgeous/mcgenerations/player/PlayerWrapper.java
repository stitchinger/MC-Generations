package io.georgeous.mcgenerations.player;

import org.bukkit.entity.Player;


public class PlayerWrapper {
    public final Player player;
    public PlayerRole playerRole;

    public boolean canCarry = false;
    public boolean canBeCarried = false;

    public PlayerWrapper(Player player) {
        this.player = player;
    }

    // Update Functions
    public void update() {
        if(playerRole != null)
            playerRole.update();
    }

}
