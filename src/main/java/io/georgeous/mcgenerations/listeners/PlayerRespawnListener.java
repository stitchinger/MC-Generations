package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.haoshoku.nick.api.NickAPI;


public class PlayerRespawnListener implements Listener {

    Player player = null;
    PlayerWrapper playerWrapper;

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        player = event.getPlayer();
        playerWrapper = PlayerManager.get().getWrapper(player);
        if(playerWrapper == null){
            PlayerManager.get().initPlayer(player);
            playerWrapper = PlayerManager.get().getWrapper(player);
        }
        playerWrapper.setLastGameMode(player.getGameMode());
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);

        new BukkitRunnable(){
            @Override
            public void run() {

                    NickAPI.resetNick(player);
                    NickAPI.resetSkin(player);
                    NickAPI.refreshPlayer(player);

            }
        }.runTaskLater(MCG.getInstance(), 20L);

        event.setRespawnLocation(MCG.council.getRandomCouncilSpawn());

        discordReminderMessage(player);
    }


    private void discordReminderMessage(Player player){
        player.sendMessage("");
        BaseComponent[] component = new ComponentBuilder("Please dont forget to join the family on ")
                .color(ChatColor.GOLD)
                .append("Discord!").color(ChatColor.BLUE).underlined(true)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/U262bxT4jh"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to join!").color(ChatColor.GRAY).italic(true).create()))
                .create();
        player.spigot().sendMessage(component);
        player.sendMessage("");
    }



}
