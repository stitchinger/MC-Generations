package io.georgeous.mcgenerations.utils;

import io.georgeous.mcgenerations.MCG;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class NameManager {

    public static final String[] firstNames =
            {
                    "Martha", "Lisa", "Julia", "Sarah", "Anna", "Maria", "Emma", "Sophia", "Charlie", "Mia", "Chloe",
                    "Vic", "Scarlett", "Amy", "Madison", "Ellie", "Natalie", "Adele", "Joy", "Olivia", "Naomi",
                    "Grace", "Ruby", "Quinn", "Lydia", "Vivian", "Hailey", "Autumn", "Aurora", "Hazel",
                    "Penelope", "Catelyn", "Freya", "Faith", "Cersei", "Sansa", "Arya", "Shae", "Talisa", "Brienne",
                    "Ellaria","Signy", "Hildr", "Nanna", "Eisa", "Saga", "Sol", "Hel", "Frigg", "Lofn", "Joro", "Jojo",
                    "Lounn", "Fulla", "Laga", "Elli"
            };
    public static final String[] lastNames =
            {
                    "Marten", "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Taylor",
                    "Moore", "White", "Walker", "Hill", "Nelson", "Baker", "Adams", "Mitchell", "Clinton", "Bush",
                    "Merkel", "Musk", "Einstein", "Simpson", "Jefferson", "Rogan", "Ronan", "Ericson", "Bjornson",
                    "Mueller", "Freundl", "Goldberg", "Lovejoy", "Francis", "Caddel", "Amana", "Barlowe", "Fox", "Hart",
                    "Hendrix", "Madden", "Sai", "Boone", "Elrod", "Hale", "Langston", "McElfresh", "Trevino", "Amos",
                    "Beam", "Dash", "Duke", "Jian", "Pierce", "Remington", "Sharp", "West", "Sand", "Snow", "Rivers",
                    "Stone", "Pyke", "Waters", "Flowers", "Crawford", "Driscoll", "Simmons", "Vandenberg", "Webb",
                    "Finch", "Abednego", "Cyprus", "Dagon", "Damaris", "Einar", "Festus", "Gallio", "Griffin", "Kami",
                    "Lightfoot", "Luna", "Priestley", "Sierra", "Stark", "Umber", "Bolton", "Karstark", "Hornwood",
                    "Talihart", "Mormont", "Dustin", "Manderly", "Flint", "Glover", "Reed", "Baelish", "Lynderly",
                    "Mallister", "Royce", "Greyjoy", "Blackwood", "Tully", "Bracken", "Whent", "Mooton", "Westerling",
                    "Marbrand", "Lefford", "Lannister", "Clegane", "Swyft", "Crakehall", "Oakheart", "Tyrell", "Selmy",
                    "Baratheon", "Tarth", "Swann", "Seaworth", "Dondarrion", "Caron", "Florent", "Tarly", "Hightower",
                    "Martell", "Redwyne"
            };

    public static ArrayList<String> usedNames = new ArrayList<>();


    public static String randomFirst() {
        if(usedNames.size() > firstNames.length / 2){
            usedNames = new ArrayList<>();
        }
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
        config.set("data.server.test", "usedNames");
        MCG.getInstance().saveConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = MCG.getInstance().getConfig();
        usedNames = (ArrayList<String>) config.getStringList("data.server.usednames");
        MCG.getInstance().getLogger().log(Level.INFO, usedNames.toString());
        //MCG.getInstance().saveConfig();
    }
}