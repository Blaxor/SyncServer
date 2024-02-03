package ro.deiutzentartainment.games.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;

import java.io.File;

public class GameHelper {
    private static Logger _logger = LogManager.getLogger(GameHelper.class);

    public static void packGame(Game game, File zip){
        if(game.existsGamePath()) {
            _logger.info("Packing the game data for "+game.getName());
            ZipUtil.pack(new File(game.getGamePath()), zip, name -> name);
            _logger.info("The packing has finished");
        }else{
            _logger.warn("There is no game data path declared. Continue");
        }
    }
    public static void packSave(Game game, File zip){
        _logger.info("Packing the save for "+game.getName());
        ZipUtil.pack(new File(game.getSavePath()), zip,  name -> name);
        _logger.info("The packing has finished");
    }
    public static void unpackGame(Game game, File zip){
        if(game.existsGamePath()){
            _logger.info("Unpacking the game data " + game.getName() + " to path " + game.getGamePath());
            File location = new File(game.getGamePath());
            if(location.exists()) {
            _logger.debug("The data already exists, deleting it.");
                FileUtils.delete(location);
            }
            _logger.debug("Started the unpacking.");
            ZipUtil.unpack(zip, location,
                    name -> name);
            _logger.info("The unpacking has finished.");
        }else{
            _logger.warn("There is no game data path declared. Continue");
        }
    }
    public static void unpackSave(Game game, File zip){
        _logger.info("Unpacking the game data " + game.getName() + " to path " + game.getSavePath());
        File location = new File(game.getSavePath());

        if(location.exists()) {
            _logger.debug("The data already exists, deleting it.");
            FileUtils.delete(location);
        }
        _logger.debug("Started the unpacking.");

        ZipUtil.unpack(zip, location,
                name -> name);
        _logger.info("The unpacking has finished.");

    }



}
