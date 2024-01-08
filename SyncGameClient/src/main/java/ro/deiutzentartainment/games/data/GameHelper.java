package ro.deiutzentartainment.games.data;

import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;

import java.io.File;

public class GameHelper {


    public static String getZipFileSavePath(){
        return "save/";
    }
    public static String getZipFileGamePath(){
        return "game/";
    }
    public static void packGame(Game game, File zip){
        if(game.existsGamePath())
            ZipUtil.pack(new File(game.getGamePath()), zip,  name -> getZipFileGamePath() + name);
    }
    public static void packSave(Game game, File zip){
        ZipUtil.pack(new File(game.getSavePath()), zip,  name -> getZipFileSavePath() + name);
    }
    public static void unpackGame(Game game, File zip){
        if(game.existsGamePath()){
            File location = new File(game.getGamePath());
            if(location.exists())
                FileUtils.delete(location);
            ZipUtil.unpack(zip, location,
                    name -> name.startsWith(getZipFileGamePath()) ? name.substring(getZipFileGamePath().length()) : name);
        }
    }
    public static void unpackSave(Game game, File zip){
        File location = new File(game.getSavePath());
        if(location.exists())
            FileUtils.delete(location);
        ZipUtil.unpack(zip, location,
                name -> name.startsWith(getZipFileSavePath()) ? name.substring(getZipFileSavePath().length()) : name);
    }



}
