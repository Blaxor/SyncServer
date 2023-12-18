package ro.deiutzentartainment.games.data;

import ro.deiutzentartainment.games.GameHandlerImpl;

import java.awt.image.BufferedImage;

public class GameImpl implements Game{

    String name,path;
    public GameImpl(String name, String path){
        this.name=name;
        this.path=path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSavePath() {
        return path;
    }

    @Override
    public BufferedImage getImage() {
        return null;
    }

    @Override
    public String toString() {
        return "GameImpl{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
