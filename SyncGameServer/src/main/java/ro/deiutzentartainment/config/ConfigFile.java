package ro.deiutzentartainment.config;

import org.apache.commons.io.FileUtils;
import ro.deiutzblaxo.cloud.fileutils.ProgramDirectoryUtilities;
import ro.deiutzblaxo.cloud.yaml.YAMLFile;
import ro.deiutzblaxo.cloud.yaml.YAMLFileImpl;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigFile {

    private static ConfigFile instance;
    public  static ConfigFile instance(){
        if(instance == null){
            instance = new ConfigFile();
        }
        return instance;
    }
    private final YAMLFile file;
    public ConfigFile(){
        file = new YAMLFileImpl(ConfigFiles.CONFIG.getPath());
        try {
            file.load();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + ConfigFiles.CONFIG + ". Loading defaults.");
            File _file = new File(ConfigFiles.CONFIG.getPath());

            try {
                FileUtils.copyURLToFile(getClass().getClassLoader().getResource("config.yaml"),_file);
                file.load();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public Object getConfig(Config config){
        Object obj = file.get(config.field);
        if(obj == null){
            return config._default;
        }
        return obj;
    }
    public boolean getBoolean(Config config){
        return (boolean) getConfig(config);
    }
    public File getTempFolder(){
        File folder = new File(ProgramDirectoryUtilities.getProgramDirectory()+getConfig(Config.TEMP_FOLDER));
        if(!folder.exists()){
            folder.mkdirs();
        }
        return folder;
    }
    public File getSaveGameFolder(){
        File folder = new File(ProgramDirectoryUtilities.getProgramDirectory()+getConfig(Config.SAVE_GAME_FOLDER));
        if(!folder.exists()){
            folder.mkdirs();
        }
        return folder;
    }
    public File getDataGameFolder(){
        File folder = new File(ProgramDirectoryUtilities.getProgramDirectory()+getConfig(Config.DATA_GAME_FOLDER));
        if(!folder.exists()){
            folder.mkdirs();
        }
        return folder;
    }
}
