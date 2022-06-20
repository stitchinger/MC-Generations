package io.georgeous.mcgenerations.adoption;

import io.georgeous.mcgenerations.systems.role.PlayerRole;

import io.georgeous.mcgenerations.utils.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class AdoptionRequest {
    final int REQUEST_VALID_TIME = 60; // seconds
    PlayerRole adopter;
    PlayerRole adoptee;
    UUID uuid;
    Long time;


    public AdoptionRequest(PlayerRole adopter, PlayerRole adoptee, UUID uuid){

        this.adopter = adopter;
        this.adoptee = adoptee;
        this.uuid = uuid;
        this.time = System.currentTimeMillis();
        sendRequestMsg();
    }

    private void sendRequestMsg(){

        Notification.neutralMsg(adoptee.getPlayer(), adopter.getName() + " " + adopter.getFamily().getColoredName() + " sent you a adoption request. Do you want to join the family " + adopter.getFamily().getColoredName() + "?");
        Notification.neutralMsg(adopter.getPlayer(), "You sent an adoption request to " + adoptee.getName() + " " + adoptee.getFamily().getColoredName() + ". Wait for their response");
        TextComponent yes = new TextComponent("YES");
        yes.setColor(ChatColor.GREEN);
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adopt request accept " + uuid));
        yes.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Join family and teleport there").color(ChatColor.GRAY).italic(true).create())
        );

        TextComponent no = new TextComponent("NO");
        no.setColor(ChatColor.RED);
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adopt request decline " + uuid));
        no.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Decline adoption request").color(ChatColor.GRAY).italic(true).create())
        );

        adoptee.getPlayer().spigot().sendMessage(new TextComponent("Click "), yes, new TextComponent(" or "),  no);
    }

    public void accept(){
        adoptee.getFamily().removeMember(adoptee);
        adopter.getFamily().addMember(adoptee);
        adoptee.setFamily(adopter.getFamily());
        adopter.setUsedAdopt(true);
        adoptee.updateScoreboard();

        adoptee.getPlayer().teleport(adopter.getPlayer().getLocation());
        Notification.successMsg(adopter.getPlayer(), "You adopted " + adoptee.getName() + ".");
        Notification.successMsg(adoptee.getPlayer(), "You are now a member of the " + adopter.getFamily().getColoredName() + " family");
    }

    public void decline(){
        Notification.neutralMsg(adopter.getPlayer(), adoptee.getName() + " " + adoptee.getFamily().getColoredName() +  " declined your adoptation request.");
        Notification.neutralMsg(adoptee.getPlayer(), "You declined the adoptation request of " + adopter.getName() + " " + adopter.getFamily().getColoredName());
    }

    public boolean isValid(){
        return time + 1000L * REQUEST_VALID_TIME > System.currentTimeMillis();
    }

}
