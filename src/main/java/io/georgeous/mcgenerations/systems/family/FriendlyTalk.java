package io.georgeous.mcgenerations.systems.family;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class FriendlyTalk {

    private static final HashMap<UUID, Long> coolDown = new HashMap<>();
    private static final String[][] sentences = {
            {"You gave $$$ a big hug", "$$$ gave you a big hug"},
            {"You told $$$ that you appreciate them", "$$$ told you, that they appreciate you"},
            {"You gave $$$ a big hug", "$$$ gave you a big hug"},
            {"You gave $$$ a big hug", "$$$ gave you a big hug"}
    };

    public static String[] getMessages(String receiver, String sender) {
        int index = (int) (Math.random() * sentences.length);
        return new String[]{insertPlaceholder(sentences[index][0], receiver), insertPlaceholder(sentences[index][1], sender)};
    }

    public static boolean isCoolDown(Player player) {
        Long previousTime = coolDown.get(player.getUniqueId());
        if (previousTime == null)
            return false;
        boolean isCoolDown = System.currentTimeMillis() < previousTime + 5000;
        if (!isCoolDown) {
            coolDown.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public static void addCoolDown(Player player) {
        coolDown.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private static String insertPlaceholder(String text, String insert) {
        return text.replace("$$$", insert);
    }
}