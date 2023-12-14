package ro.deiutzentartainment.games;

import ro.deiutzentartainment.games.data.Game;

public interface GameHandler {

    void saveGame(Game game);
    void loadGame(Game game);

    String getIdHandler();



}
