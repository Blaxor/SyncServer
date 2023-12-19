package ro.deiutzentartainment;

import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;
import ro.deiutzentartainment.games.GameHandler;
import ro.deiutzentartainment.games.GameHandlerImpl;
import ro.deiutzentartainment.games.data.Game;
import ro.deiutzentartainment.games.data.GameImpl;
import ro.deiutzentartainment.games.data.SavePathGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        GameHandler gameHandler = new GameHandlerImpl(ConfigConnection.getInstance());


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean close = false;

        while(!close) {
            String input = null;
            try {
                System.out.println("Please input the next operation save or load.");
                input = reader.readLine();
                input = input.toLowerCase();
                switch (input) {

                    case "save" -> {
                        System.out.println("Please share the game name: ");
                        String gameName = reader.readLine().toLowerCase();
                        Optional<SavePathGame> opt = Arrays.stream(SavePathGame.values())
                                .filter(savePathGame -> savePathGame.name.equalsIgnoreCase(gameName)).findAny();
                        if(opt.isEmpty()) {
                            System.out.println("The game is not found, continue");
                            continue;
                        }else{
                            System.out.println("Saving game...");
                            gameHandler.saveGame(SavePathGame.generateGame(opt.get()));

                        }
                        break;
                    }
                        case "load" ->{
                            System.out.println("Please share the game name: ");
                            String gameName = reader.readLine().toLowerCase();
                            Optional<SavePathGame> opt = Arrays.stream(SavePathGame.values())
                                    .filter(savePathGame -> savePathGame.name.equalsIgnoreCase(gameName)).findAny();
                            if(opt.isEmpty()) {
                                System.out.println("The game is not found, continue");
                                continue;
                            }else{
                                System.out.println("Loading game...");
                                gameHandler.loadGame(SavePathGame.generateGame(opt.get()));

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