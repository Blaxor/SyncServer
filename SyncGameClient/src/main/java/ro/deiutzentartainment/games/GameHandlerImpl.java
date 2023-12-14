package ro.deiutzentartainment.games;

import lombok.SneakyThrows;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;
import ro.deiutzentartainment.connection.RequestHandler;
import ro.deiutzentartainment.fileutils.ProgramDirectoryUtilities;
import ro.deiutzentartainment.games.data.Game;

import java.util.UUID;

public class GameHandlerImpl implements GameHandler {

    private UUID id = UUID.randomUUID();
    RequestHandler requestHandler;
    private String TEMP_FILE_PATH = ProgramDirectoryUtilities.getProgramDirectory() + "/temp/";
    public GameHandlerImpl(ConfigConnection configConnection){
        this.requestHandler = new RequestHandler(
                (String) configConnection.getConfig(Config.IP_ADDRESS),
                (Integer) configConnection.getConfig(Config.PORT));

    }
    @Override
    public void saveGame(Game game) {
        System.out.println("Saving the game "+ game.getName());
        requestHandler.putGameSave(game);
    }

    @SneakyThrows
    @Override
    public void loadGame(Game game) {
        System.out.println("loading the game "+ game.getName());
        requestHandler.getGameSave(game);


    }

    @Override
    public String getIdHandler() {
        return id.toString();
    }

}
