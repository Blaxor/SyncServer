package ro.deiutzentartainment.connection.handler;

import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigFile;

import java.io.File;
import java.util.UUID;

public interface ConnectionHandler {

    int SIZE_PACKET = (int) ConfigFile.instance().getConfig(Config.PACKET_SIZE);

    void Start();

    void Stop();

    default File getFile(String name, boolean createFolders, int type){
        File folderSave =  ( type == 0 ? ConfigFile.instance().getSaveGameFolder() : ConfigFile.instance().getDataGameFolder()) ;
        if(!folderSave.exists() & createFolders)
            folderSave.mkdirs();
        return new File(folderSave,name+".zip");
    }

}
