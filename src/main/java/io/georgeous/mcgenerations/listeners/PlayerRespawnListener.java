package io.georgeous.mcgenerations.listeners;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.files.McgConfig;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.systems.role.RoleManager;
import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.haoshoku.nick.api.NickAPI;


public class PlayerRespawnListener implements Listener {

    Player player = null;
    PlayerWrapper playerWrapper = null;

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        player = event.getPlayer();
        playerWrapper = PlayerManager.get().getWrapper(player);
        //player.sendMessage(player.getHealth() + "");

        if(player.getHealth() != 0){
            // End portal
            return;
        }

        setupPlayerForCouncil(player);
        respawnToCouncil(event);
        //event.setRespawnLocation(player.getLocation());
        //player.setGameMode(GameMode.SPECTATOR);
    }

    private void respawnToCouncil(PlayerRespawnEvent event){
        if(playerWrapper == null){
            PlayerManager.get().initPlayer(player);
            playerWrapper = PlayerManager.get().getWrapper(player);
        }
        playerWrapper.setLastGameMode(player.getGameMode());
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
        setupPlayerForCouncil(player);

        event.setRespawnLocation(MCG.council.getRandomCouncilSpawn());
        discordReminderMessage(player);

        new BukkitRunnable(){
            @Override
            public void run() {
                NickAPI.resetNick(player);
                NickAPI.resetSkin(player);
                NickAPI.refreshPlayer(player);

                //addToQueue(playerWrapper);

            }
        }.runTaskLater(MCG.getInstance(), 20L);
    }

    private void addToQueue(PlayerWrapper pw){

        Family family = pw.getLastFamily();
        if(family == null) return;

        if(!pw.getDiedOfOldAge()) return;

        family.getBabyQueue().add(pw.getPlayer());

        Notification.neutralMsg(player, "You have been added to the birthing queue of the " + family.getName() + " family");
    }

    private void setupPlayerForCouncil(Player player){
        playerWrapper = PlayerManager.get().getWrapper(player);
        if(playerWrapper == null){
            PlayerManager.get().initPlayer(player);
            playerWrapper = PlayerManager.get().getWrapper(player);
        }
        playerWrapper.setLastGameMode(player.getGameMode());
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
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
