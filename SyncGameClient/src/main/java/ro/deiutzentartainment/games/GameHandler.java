package ro.deiutzentartainment.games;

import ro.deiutzentartainment.games.data.Game;

public interface GameHandler {

    void saveGameSave(Game game);
    void loadGameSave(Game game);

    void saveGameData(Game game);

    void loadGameData(Game game);

    String getIdHandler();



}
