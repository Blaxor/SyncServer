package ro.deiutzentartainment.config;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.deiutzblaxo.cloud.fileutils.ProgramDirectoryUtilities;
import ro.deiutzblaxo.cloud.yaml.YAMLFile;
import ro.deiutzblaxo.cloud.yaml.YAMLFileImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigConnection {

    private static ConfigConnection instance;
    private static Logger _logger = LogManager.getLogger(ConfigConnection.class);
    public static ConfigConnection getInstance(){
        if(instance == null){
            instance = new ConfigConnection();
        }
        return instance;
    }
    private final YAMLFile file;
    public ConfigConnection(){
        instance =this;
        file = new YAMLFileImpl(ConfigFiles.CONFIG_CONNECTION.getPath());
        try {
            file.load();
        } catch (FileNotFoundException e) {
            _logger.warn("File not found: " + ConfigFiles.CONFIG_CONNECTION + ". Loading defaults.");
            File _file = new File(ConfigFiles.CONFIG_CONNECTION.getPath());

            try {
                FileUtils.copyURLToFile(getClass().getClassLoader().getResource("config_connection.yaml"),_file);
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
        return new File(ProgramDirectoryUtilities.getProgramDirectory()+getConfig(Config.TEMP_FOLDER));
    }
}
