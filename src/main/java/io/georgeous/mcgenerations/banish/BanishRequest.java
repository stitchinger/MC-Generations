package io.georgeous.mcgenerations.banish;

import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanishRequest {
    final int REQUEST_VALID_TIME = 60; // seconds
    PlayerRole banisher;
    PlayerRole banished;
    Family family;
    UUID uuid;
    Long time;
    List<Player> votes;
    int votesYes = 0;
    int votesNo = 0;


    public BanishRequest(PlayerRole banisher, PlayerRole banished, UUID uuid) {

        this.banisher = banisher;
        this.banished = banished;
        this.family = banisher.getFamily();
        this.uuid = uuid;
        this.time = System.currentTimeMillis();
        this.votes = new ArrayList<>();
        sendRequestMsg();
    }

    private void sendRequestMsg() {
        family.getMembers().forEach(member -> {
            member.getPlayer().sendMessage("test123");
            Notification.neutralMsg(member.getPlayer(), banisher.getName() + " requested to banish " + banished.getName());
            Notification.neutralMsg(member.getPlayer(), "You got 1 minute to vote");
            TextComponent yes = getYesComponent();
            TextComponent no = getNoComponent();

            member.getPlayer().spigot().sendMessage(new TextComponent("Click "), yes, new TextComponent(" or "), no);
        });

    }

    private TextComponent getYesComponent(){
        TextComponent yes = new TextComponent("YES");
        yes.setColor(ChatColor.GREEN);
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/banish request accept " + uuid));
        yes.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Vote Yes").color(ChatColor.GRAY).italic(true).create())
        );
        return  yes;
    }

    private TextComponent getNoComponent(){
        TextComponent no = new TextComponent("NO");
        no.setColor(ChatColor.RED);
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/banish request decline " + uuid));
        no.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Vote No").color(ChatColor.GRAY).italic(true).create())
        );
        return no;
    }

    public void accept(Player player) {
        votesYes++;
        votes.add(player);

        family.getMembers().forEach(member -> {
            Notification.neutralMsg(member.getPlayer(), "YES: " + votesYes + " | NO: " + votesNo);
        });

    }

    public void decline(Player player) {
        votesNo++;
        votes.add(player);

        family.getMembers().forEach(member -> {
            Notification.neutralMsg(member.getPlayer(), "YES: " + votesYes + " | NO: " + votesNo);
        });
    }

    public boolean isTimeout() {
        return System.currentTimeMillis() > time + 1000L * REQUEST_VALID_TIME;
    }

    public List<Player> getVotes(){
        return votes;
    }



}
