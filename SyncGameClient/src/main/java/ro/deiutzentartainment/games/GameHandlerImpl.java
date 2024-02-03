package ro.deiutzentartainment.games;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;
import ro.deiutzentartainment.connection.RequestHandler;
import ro.deiutzentartainment.games.data.Game;

import java.util.UUID;

public class GameHandlerImpl implements GameHandler {
    private static Logger _logger = LogManager.getLogger(GameHandlerImpl.class);

    private UUID id = UUID.randomUUID();

    RequestHandler requestHandler;

    public GameHandlerImpl(ConfigConnection configConnection){
        this.requestHandler = new RequestHandler(
                (String) configConnection.getConfig(Config.IP_ADDRESS),
                (Integer) configConnection.getConfig(Config.PORT));

    }
    @Override
    public void saveGameSave(Game game) {
        _logger.info("Saving the game save "+ game.getName());
        requestHandler.putGameSave(game);
        _logger.info("The game save has been finished for "+ game.getName());

    }

    @SneakyThrows
    @Override
    public void loadGameSave(Game game) {
        _logger.info("Loading the game save "+ game.getName());
        requestHandler.getGameSave(game);
        _logger.info("The game save of " +game.getName() + " has been saved.");


    }

    @Override
    public void saveGameData(Game game) {
        _logger.info("Saving the game data "+ game.getName());
        requestHandler.putGameData(game);


    }

    @Override
    public void loadGameData(Game game) {
        _logger.info("Loading the game data "+ game.getName());
        requestHandler.getGameData(game);

    }

    @Override
    public String getIdHandler() {
        return id.toString();
    }

}
