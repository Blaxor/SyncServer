package ro.deiutzentartainment;

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
    public static void main(String[] args) {

        GameHandler gameHandler = new GameHandlerImpl(ConfigConnection.getInstance());

        GameManager games = GameManager.getInstance();


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean close = false;

        while(!close) {
            String input = null;
            try {
                System.out.println("Please input the next operation save or load, game.");
                input = reader.readLine();
                input = input.toLowerCase();
                switch (input) {

                    case "save" -> {
                        System.out.println("Please share the game name: ");
                        String gameName = reader.readLine().toLowerCase();
                        Game game = games.getGame(gameName);
                        if(game==null) {
                            System.out.println("The game is not found, continue");
                            continue;
                        }else{
                            System.out.println("Saving game...");
                            gameHandler.saveGameSave(game);

                        }
                        break;
                    }
                    case "load" ->{
                            System.out.println("Please share the game name: ");
                            String gameName = reader.readLine().toLowerCase();
                            Game game =games.getGame(gameName);
                            if(game == null){
                                System.out.println("Game not found.");
                            }else {
                                System.out.println("Loading game...");
                                gameHandler.loadGameSave(game);
                            }

                            break;
                        }
                        case "game" ->{
                        System.out.println("Select add,remove,list or info");
                            input = reader.readLine().toLowerCase();

                            switch (input){
                                case "add"->{
                                    System.out.println("Selected to add a game to the list, please follow the instructions:");
                                    System.out.println("Game name");
                                    String gameName = reader.readLine().toLowerCase();
                                    System.out.println("Save path");
                                    String savePath = reader.readLine();
                                    System.out.println("Game Path");
                                    String gamePath = reader.readLine();
                                    try {
                                        games.addGame(gameName, new GameImpl(gameName,savePath,gamePath));
                                    } catch (InvalidNameException e) {
                                        System.out.println("Invalid name, please try again");
                                        break;
                                    }
                                    System.out.println("Game added : " + new GameImpl(gameName,savePath,gamePath));
                                    break;
                                }
                                case "list"->{
                                    System.out.println("The list of the games is: " +games.getGameList());
                                }
                                case "info"->{
                                    System.out.println("Select the game name: ");
                                    String gameName = reader.readLine();
                                    System.out.println("The game proprieties are: " +games.getInfo(gameName));
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