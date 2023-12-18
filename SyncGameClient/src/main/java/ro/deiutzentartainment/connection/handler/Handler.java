package ro.deiutzentartainment.connection.handler;

import ro.deiutzblaxo.cloud.fileutils.ProgramDirectoryUtilities;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;

import ro.deiutzentartainment.games.data.Game;

import java.io.File;

public interface Handler {

    void Start();
    void Stop();

    default File getGameSaveFolder(){
        return new File(getGame().getSavePath());

    };

    default void generateTempFolder(){
        String tempFolder = (String) ConfigConnection.getInstance().getConfig(Config.TEMP_FOLDER);
        File tempFolderFile = new File(ProgramDirectoryUtilities.getProgramDirectory() +tempFolder);
        System.out.println("TempFolder + " + tempFolderFile.getPath());
        if(!tempFolderFile.exists())
            tempFolderFile.mkdirs();
        setTempFolder(tempFolderFile);
    }
    File getTempFolder();

    void setTempFolder(File file);

    Game getGame();
    int getPacketSize();

}
