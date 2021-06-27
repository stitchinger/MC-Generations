package io.georgeous.mcgenerations.lifephase;

import io.georgeous.mcgenerations.manager.SurroManager;
import io.georgeous.mcgenerations.player.PlayerWrapper;
import org.bukkit.entity.Player;

public class TeenPhase extends LifePhase{

    private Player player;
    private PlayerWrapper playerWrapper;

    public TeenPhase(Player player, PlayerWrapper playerWrapper){
        this.player = player;
        this.playerWrapper = playerWrapper;
        this.skinID = "297371";
    }

    @Override
    public void start() {
        player.sendMessage("You are a §ateen");
        playerWrapper.playerRole.receiveBabyHandler();
        //NameManager.changeSkin(this.player, PhaseManager.skinIds[3]);
        SurroManager.destroySurrogate(player);
    }

    @Override
    public void end() {

    }

    @Override
    public void update() {

    }
}
