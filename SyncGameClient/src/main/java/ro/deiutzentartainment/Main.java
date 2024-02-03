package ro.deiutzentartainment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.deiutzentartainment.config.ConfigConnection;
import ro.deiutzentartainment.exceptions.gamefile.InvalidNameException;
import ro.deiutzentartainment.games.GameHandler;
import ro.deiutzentartainment.games.GameHandlerImpl;
import ro.deiutzentartainment.games.GameManager;
import ro.deiutzentartainment.games.data.Game;
import ro.deiutzentartainment.games.data.GameImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static Logger _logger = LogManager.getLogger(Main.class.getName());
    public static void main(String[] args) {

        GameHandler gameHandler = new GameHandlerImpl(ConfigConnection.getInstance());
        GameManager games = GameManager.getInstance();


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean close = false;

        while(!close) {
            String input = null;
            try {
                _logger.info("Please input the next operation save or load, game.");
                input = reader.readLine().toLowerCase();
                switch (input) {

                    case "save" -> {

                        _logger.info("'game data', 'save data','all'");

                        input = reader.readLine().toLowerCase();
                        if(input.equalsIgnoreCase("game data")){
                            _logger.info("Please share the game name: ");
                            String gameName = reader.readLine().toLowerCase();
                            Game game = games.getGame(gameName);
                            if(game==null) {
                                _logger.info("The game is not found, continue");
                                continue;
                            }else{
                                _logger.info("Saving game...");
                                gameHandler.saveGameData(game);

                            }
                        }else if(input.equalsIgnoreCase("all")){
                            _logger.info("Please share the game name: ");
                            String gameName = reader.readLine().toLowerCase();
                            Game game = games.getGame(gameName);
                            if (game == null) {
                                _logger.info("Game not found.");
                            } else {
                                _logger.info("Loading save data...");
                                gameHandler.saveGameSave(game);
                                _logger.info("Loading game data...");
                                gameHandler.saveGameData(game);
                            }
                        }else{
                            _logger.info("Please share the game name: ");
                            String gameName = reader.readLine().toLowerCase();
                            Game game = games.getGame(gameName);
                            if(game==null) {
                                _logger.info("The game is not found, continue");
                                continue;
                            }else{
                                _logger.info("Saving game...");
                                gameHandler.saveGameSave(game);

                            }
                        }



                        break;
                    }
                    case "load" -> {
                        _logger.info("'game data', 'save data','all'");

                        input = reader.readLine().toLowerCase();

                            if (input.equalsIgnoreCase("game data")) {
                                _logger.info("Please share the game name: ");
                                String gameName = reader.readLine().toLowerCase();
                                Game game = games.getGame(gameName);
                                if (game == null) {
                                    _logger.info("Game not found.");
                                } else {
                                    _logger.info("Loading game...");
                                    gameHandler.loadGameData(game);
                                }
                            } else if(input.equalsIgnoreCase("all")){
                                _logger.info("Please share the game name: ");
                                String gameName = reader.readLine().toLowerCase();
                                Game game = games.getGame(gameName);
                                if (game == null) {
                                    _logger.info("Game not found.");
                                } else {
                                    _logger.info("Loading save data...");
                                    gameHandler.loadGameSave(game);
                                    _logger.info("Loading game data...");
                                    gameHandler.loadGameData(game);
                                }


                            }else{
                                _logger.info("Please share the game name: ");
                                String gameName = reader.readLine().toLowerCase();
                                Game game = games.getGame(gameName);
                                if (game == null) {
                                    _logger.info("Game not found.");
                                } else {
                                    _logger.info("Loading game...");
                                    gameHandler.loadGameSave(game);
                                }
                            }
                                break;

                    }
                        case "game" ->{
                        _logger.info("Select add,remove,list or info");
                            input = reader.readLine().toLowerCase();

                            switch (input){
                                case "add"->{
                                    _logger.info("Selected to add a game to the list, please follow the instructions:");
                                    _logger.info("Game name");
                                    String gameName = reader.readLine().toLowerCase();
                                    _logger.info("Save path");
                                    String savePath = reader.readLine();
                                    _logger.info("Game Path");
                                    String gamePath = reader.readLine();
                                    try {
                                        games.addGame(gameName, new GameImpl(gameName,savePath,gamePath));
                                    } catch (InvalidNameException e) {
                                        _logger.info("Invalid name, please try again");
                                        break;
                                    }
                                    _logger.info("Game added : " + new GameImpl(gameName,savePath,gamePath));
                                    break;
                                }
                                case "list"->{
                                    _logger.info("The list of the games is: " +games.getGameList());
                                }
                                case "info"->{
                                    _logger.info("Select the game name: ");
                                    String gameName = reader.readLine();
                                    _logger.info("The game proprieties are: " +games.getInfo(gameName));
                                }

                            }
                        }


            }


        } catch (IOException e) {
                e.printStackTrace();
                continue;
            }


        }
    }

}