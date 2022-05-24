package io.georgeous.mcgenerations.files;

public class FileManager {

    private CFGFile scoreboardFile;

    public FileManager(String dataPath) {

        scoreboardFile = new CFGFile(dataPath + "/", "scoreboard.yml");
    }

    public CFGFile getScoreboardFile() {
        return scoreboardFile;
    }
}
