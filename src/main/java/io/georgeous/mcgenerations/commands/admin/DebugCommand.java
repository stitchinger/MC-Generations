package io.georgeous.mcgenerations.commands.admin;


import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.commands.CommandUtils;
import io.georgeous.mcgenerations.files.McgConfig;

import io.georgeous.mcgenerations.scoreboard.ScoreboardHandler;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.Top10;
import io.georgeous.mcgenerations.systems.player.PlayerManager;
import io.georgeous.mcgenerations.systems.player.PlayerWrapper;

import io.georgeous.mcgenerations.utils.ItemManager;
import io.georgeous.mcgenerations.utils.NameManager;
import io.georgeous.mcgenerations.utils.Notification;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.ArrayList;
import java.util.List;

public class DebugCommand implements CommandExecutor, TabCompleter {

    public DebugCommand() {
        CommandUtils.addTabComplete("debug", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.isOp()) {
            Notification.onlyForOp(player);
            return true;
        }

        if (args.length == 0) {
            setDebug(player);
            return true;
        }

        if ("true".equals(args[0])) {
            setDebug(player, true);
            return true;
        }

        if ("false".equals(args[0])) {
            setDebug(player, false);
            return true;
        }

        if ("council".equals(args[0])) {
            player.teleport(McgConfig.getCouncilLocation());
            return true;
        }

        if ("refresh".equals(args[0])) {
            ScoreboardHandler.get().refreshScoreboardOfPlayer(player);
            return true;
        }

        if ("clearnames".equals(args[0])) {
            NameManager.usedNames = new ArrayList<>();
            return true;
        }


        if ("speed".equals(args[0])) {
            AttributeInstance speed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            player.sendMessage(speed.getBaseValue() + "");
            player.sendMessage(speed.getDefaultValue() + "");
            player.sendMessage(speed.getValue() + "");
            //player.setWalkSpeed(0.7f);
            //speed.setBaseValue(speed.getDefaultValue());
            return true;
        }

        if ("reloadconfig".equals(args[0])) {
            McgConfig.reload();
            return true;
        }

        if ("isnick".equals(args[0])) {
            player.sendMessage(String.valueOf(NickAPI.isNickedName(args[1])));
            ;
            //McgConfig.reload();
            return true;
        }

        if ("stat".equals(args[0])) {
            PlayerWrapper wrapper = PlayerManager.get().getWrapper(player);
            player.sendMessage("Old age: " + wrapper.getDiedOfOldAge());
            if(player.getBedSpawnLocation() != null){
                player.sendMessage("Bed spawn: " + player.getBedSpawnLocation().toString());
            }


            return true;
        }

        if ("vanish".equals(args[0])) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.hidePlayer(MCG.getInstance(), player);
            });

            return true;
        }

        if ("item".equals(args[0])) {
            ItemStack item = new ItemStack(Material.SHULKER_SHELL);
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(1);
            meta.setDisplayName(ChatColor.RESET + "Diamond Dust");
            item.setItemMeta(meta);

            player.getInventory().addItem(item);

            return true;
        }

        if ("show".equals(args[0])) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.showPlayer(MCG.getInstance(), player);
            });

            return true;
        }

        if ("egg".equals(args[0])) {
            player.getInventory().addItem(ItemManager.createAnimalIncubator());

            return true;
        }

        if ("top10".equals(args[0])) {
            player.sendMessage("Best Families");
            Family[] list = Top10.get().getCurrentTop10();
            for(int i = 0; i < list.length; i++){
                Family f = list[0];
                if(f != null){
                    player.sendMessage(i + "# " + f.getColoredName() + " Gens: " + f.getMaxGenerations());
                }
            }


            return true;
        }


        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> l = new ArrayList<>();
        if (sender instanceof Player) {
            l.add("council");
        }
        return l;
    }

    private void setDebug(Player player, boolean value){
        PlayerWrapper wrapper = PlayerManager.get().getWrapper(player);
        if (wrapper == null) {
            return;
        }
        if(wrapper.isDebugMode() != value){
            wrapper.setDebugMode(value);
            Notification.successMsg(player, "Debug Mode: " + wrapper.isDebugMode());
        }else{
            Notification.errorMsg(player, "Debug Mode is already " + wrapper.isDebugMode());
        }
    }

    private void setDebug(Player player){
        PlayerWrapper playerWrapper = PlayerManager.get().getWrapper(player);
        setDebug(player, !playerWrapper.isDebugMode());
    }
}