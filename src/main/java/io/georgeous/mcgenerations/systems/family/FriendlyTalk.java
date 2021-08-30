package io.georgeous.mcgenerations.systems.family;

import io.georgeous.mcgenerations.systems.role.PlayerRole;

import java.util.List;

public class FriendlyTalk {

    private final PlayerRole sender;
    private final PlayerRole receiver;
    public static List<String[]> sentences;
    private final int index;

    public FriendlyTalk(PlayerRole sender, PlayerRole receiver) {
        this.sender = sender;
        this.receiver = receiver;

        initSentences();
        index = (int) (Math.random() * sentences.size());
    }

    public String getSenderMessage() {
        return insertPlaceholder(sentences.get(index)[0], receiver.getName());
    }

    public String getReceiverMessage() {
        return insertPlaceholder(sentences.get(index)[1], sender.getName());
    }

    private void initSentences() {
        String[] one = {"You gave $$$ a big hug", "$$$ gave you a big hug"};
        sentences.add(one);
        String[] two = {"You told $$$ that you appreciate them", "$$$ told you, that they appreciate you"};
        sentences.add(two);
        String[] three = {"You gave $$$ a big hug", "$$$ gave you a big hug"};
        sentences.add(three);
        String[] four = {"You gave $$$ a big hug", "$$$ gave you a big hug"};
        sentences.add(four);
    }

    public String insertPlaceholder(String text, String insert) {
        return text.replace("$$$", insert);
    }
}