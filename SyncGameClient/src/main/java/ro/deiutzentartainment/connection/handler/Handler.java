package ro.deiutzentartainment.connection.handler;

import ro.deiutzblaxo.cloud.fileutils.ProgramDirectoryUtilities;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;

import ro.deiutzentartainment.config.ConfigFiles;
import ro.deiutzentartainment.games.data.Game;

import java.io.File;

public interface Handler {
    int SIZE_PACKET = (int) ConfigConnection.getInstance().getConfig(Config.BATCH_SIZE);
    String FILE_EXTENSION = ".zip";
    void Start();
    void Stop();


    default File getTempFile(){
        File tempFolder = ConfigConnection.getInstance().getTempFolder();
        if(!tempFolder.exists())
            tempFolder.mkdirs();
        return new File(tempFolder,"temp" + FILE_EXTENSION);
    }



}
