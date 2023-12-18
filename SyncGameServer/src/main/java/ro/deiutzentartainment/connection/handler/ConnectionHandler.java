package ro.deiutzentartainment.connection.handler;

import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.ConfigFile;

import java.io.File;
import java.util.UUID;

public interface ConnectionHandler {

    void Start();

    void Stop();

    UUID getUUID();

    void setTempFolder(File temp);

    File getTempFolder();


    default File getSaveFile(String name, boolean createFolders){
        File folder = new File(ConfigFile.instance().getSaveGameFolder().toURI());
        if(createFolders)
            if(!folder.exists())
                folder.mkdirs();
        return new File(folder.getPath(),name+".zip");
    }
    default void generateTempFolder(){
        setTempFolder(new File(ConfigFile.instance().getTempFolder().getPath()+ "/" + getUUID()));
        if(!getTempFolder().exists()){
            getTempFolder().mkdirs();
        }
    }
    default void deleteTempFolder(){
        if(getTempFolder() != null){
            FileUtils.delete(getTempFolder());
        }
    }
    int getPacketSize();



}
