package ro.deiutzentartainment;

import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;
import ro.deiutzentartainment.games.GameHandlerImpl;
import ro.deiutzentartainment.games.data.Game;
import ro.deiutzentartainment.games.data.GameImpl;
import ro.deiutzentartainment.games.data.SavePathGame;

import java.time.LocalDateTime;
import java.util.UUID;

public class Main {
    static ConfigConnection configConnection;
    public static void main(String[] args) {

        configConnection = new ConfigConnection();
        GameHandlerImpl gameHandler = new GameHandlerImpl(configConnection);


    }

}