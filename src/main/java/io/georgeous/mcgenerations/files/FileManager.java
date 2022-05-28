package io.georgeous.mcgenerations.files;

public class FileManager {

    private CFGFile scoreboardFile;
    private CFGFile playerDataFile;

    public FileManager(String dataPath) {
        scoreboardFile = new CFGFile(dataPath + "/", "scoreboard.yml");
        playerDataFile = new CFGFile(dataPath + "/", "playerdata.yml");
    }

    public CFGFile getScoreboardFile() {
        return scoreboardFile;
    }

    public CFGFile getPlayerDataFile() {
        return playerDataFile;
    }
}
