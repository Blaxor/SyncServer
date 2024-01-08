package ro.deiutzentartainment.games.data;

import lombok.ToString;
import ro.deiutzentartainment.games.GameHandlerImpl;

import java.awt.image.BufferedImage;

@ToString
public class GameImpl implements Game{

    String name,savePath,gamePath;
    public GameImpl(String name, String savePath){
        this.name=name;
        this.savePath=savePath;
        this.gamePath="";
    }
    public GameImpl(String name, String savePath, String gamePath){
        this.name=name;
        this.savePath=savePath;
        this.gamePath=gamePath;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSavePath() {
        return replacePlaceholders(savePath);
    }
    @Override
    public String getRawSavePath() {
        return savePath;
    }

    @Override
    public String getGamePath() {
        return replacePlaceholders(gamePath);
    }

    @Override
    public String getRawGamePath() {
        return gamePath;
    }
    @Override
    public boolean existsGamePath() {
        return gamePath!= "";
    }





    private String replacePlaceholders(String string){

        return string.replaceFirst("#OS_USER_NAME#",System.getenv("USERNAME"));

    }


}
