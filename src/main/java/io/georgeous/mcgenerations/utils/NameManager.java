package io.georgeous.mcgenerations.utils;

import io.georgeous.mcgenerations.MCG;
import io.georgeous.mcgenerations.systems.family.Family;
import io.georgeous.mcgenerations.systems.family.FamilyManager;
import io.georgeous.mcgenerations.systems.role.PlayerRole;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NameManager {

    public static final String[] firstNames = {"Martha", "Lisa", "Julia", "Sarah", "Anna", "Maria", "Emma", "Sophia", "Charlotte", "Mia", "Chloe", "Victoria", "Scarlett", "Amy", "Madison", "Ellie", "Natalie", "Adele", "Joy", "Olivia", "Naomi", "Grace", "Ruby", "Quinn", "Lydia", "Vivian", "Hailey", "Autumn", "Svenja", "Aurora", "Hazel", "Penelope"};
    public static final String[] lastNames = {"Marten", "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Taylor", "Moore", "White", "Walker", "Hill", "Nelson", "Baker", "Adams", "Mitchell", "Clinton", "Bush", "Merkel", "Musk", "Einstein", "Simpson", "Jefferson", "Rogan", "Ronan", "Ericson", "Bjornson", "Mueller", "Freundl", "Goldberg", "Lovejoy"};

    public static ArrayList<String> usedNames = new ArrayList<>();


    public static String randomFirst() {
        String newName = "";
        do{
            int i = (int) (Math.random() * (firstNames.length - 1));
            newName = firstNames[i];
        }while(nameInUse(newName));
        registerName(newName);
        return newName;
    }

    public static String randomLast() {
        int i = (int) (Math.random() * (lastNames.length - 1));
        return lastNames[i];
    }

    public static boolean nameInUse(String newName){
        for (String s : usedNames) {
            if (s.contains(newName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static void registerName(String name){
        usedNames.add(name.toLowerCase());
    }

    public static void deregisterName(String name){
        usedNames.remove(name.toLowerCase());
    }

    public static void saveConfig() {
        FileConfiguration config = MCG.getInstance().getConfig();
        config.set("data.server.usednames", usedNames);
        MCG.getInstance().saveConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = MCG.getInstance().getConfig();
        usedNames = (ArrayList<String>) config.getStringList("data.server.usednames");
        //MCG.getInstance().saveConfig();
    }
}