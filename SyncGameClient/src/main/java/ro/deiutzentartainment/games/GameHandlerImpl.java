package ro.deiutzentartainment.games;

import lombok.SneakyThrows;
import ro.deiutzblaxo.cloud.fileutils.ProgramDirectoryUtilities;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;
import ro.deiutzentartainment.connection.RequestHandler;
import ro.deiutzentartainment.games.data.Game;

import java.util.UUID;

public class GameHandlerImpl implements GameHandler {

    private UUID id = UUID.randomUUID();
    RequestHandler requestHandler;
    public GameHandlerImpl(ConfigConnection configConnection){
        this.requestHandler = new RequestHandler(
                (String) configConnection.getConfig(Config.IP_ADDRESS),
                (Integer) configConnection.getConfig(Config.PORT));

    }
    @Override
    public void saveGameSave(Game game) {
        System.out.println("Saving the game save "+ game.getName());
        requestHandler.putGameSave(game);
    }

    @SneakyThrows
    @Override
    public void loadGameSave(Game game) {
        System.out.println("loading the game save "+ game.getName());
        requestHandler.getGameSave(game);


    }

    @Override
    public void saveGameData(Game game) {

    }

    @Override
    public void loadGameData(Game game) {

    }

    @Override
    public String getIdHandler() {
        return id.toString();
    }

}
