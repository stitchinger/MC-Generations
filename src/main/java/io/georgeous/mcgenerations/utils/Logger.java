package io.georgeous.mcgenerations.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Logger {

    private static final String logPrefix = "[1H1L]  ";


    public static void log(String msg){
        Bukkit.getLogger().info(logPrefix + msg);
    }
}
