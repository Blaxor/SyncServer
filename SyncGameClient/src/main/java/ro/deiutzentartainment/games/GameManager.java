package ro.deiutzentartainment.games;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.deiutzblaxo.cloud.fileutils.ProgramDirectoryUtilities;
import ro.deiutzentartainment.exceptions.gamefile.InvalidDataException;
import ro.deiutzentartainment.exceptions.gamefile.InvalidNameException;
import ro.deiutzentartainment.exceptions.gamefile.InvalidSavePathException;
import ro.deiutzentartainment.games.data.Game;
import ro.deiutzentartainment.games.data.GameImpl;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GameManager {

    private static  GameManager instance;

    private static Logger _logger = LogManager.getLogger(GameManager.class);
    private static final String GAME_FILE_PATH = ProgramDirectoryUtilities.getProgramDirectory() + "/games.txt";

    private HashMap<String , Game> games = new HashMap<>();

    public static GameManager getInstance() {
        if(instance==null)
            new GameManager();
        return instance;
    }


    public GameManager(){
        instance = this;
        File file = new File(GAME_FILE_PATH);
        if(!file.exists()) {
            File folder = file.getParentFile();
            if(!folder.exists())
                folder.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                _logger.error("Error creating the games file " + GAME_FILE_PATH, e);
            }
            try {
                setDefaults(file);
            } catch (IOException e) {
                _logger.error("Error copying the defaults into " + GAME_FILE_PATH, e);
            }

        }
        try {
            importFromFile(file);
        } catch (IOException e) {
            _logger.error("Unable to read from the file " + GAME_FILE_PATH, e);
        } catch (InvalidDataException e) {
            _logger.error(e.getMessage(), e);

        }

    }

    public Game getGame(String name){
        return games.get(name.toLowerCase());
    }


    public void addGame(String gameName, Game game) throws InvalidNameException {
        if(games.containsKey(gameName.toLowerCase()))
            throw new InvalidNameException(gameName);
        try(FileWriter fw = new FileWriter(GAME_FILE_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(game.getExportInfo());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        games.put(gameName.toLowerCase(),game);

    }

    private void setDefaults(File file) throws IOException {
        FileUtils.copyURLToFile(getClass().getClassLoader().getResource("games.txt"),file);
    }

    private void importFromFile(File file) throws IOException, InvalidDataException {
        _logger.info("Importing from the file " + file.getPath() );
        List<String> lines = Files.readAllLines(file.toPath());
        for(int i = 0 ; i < lines.size();i++){

            if(lines.get(i).startsWith("#"))
                continue;
            _logger.debug("Processing the line with number " + i);
            String[] chunks = lines.get(i).split(";");
            String name = chunks[0];
            if(name.isBlank()){
                throw new InvalidNameException(i);
            }
            String savePath = chunks[1];
            if(savePath.isBlank()){
                throw new InvalidSavePathException(i);
            }
            String dataPath = "";
            if(chunks.length >= 3)
                dataPath = chunks[2];
            _logger.debug("Adding the game following game: "+name.toLowerCase());
            games.put(name.toLowerCase(),new GameImpl(name,savePath,dataPath));
        }

    }

    public String getGameList() {
        return games.keySet().stream().map(s -> games.get(s).getName()).collect(Collectors.joining(", "));
    }

    public String getInfo(String gameName) {
        return games.get(gameName.toLowerCase()).getPrettyWritten();
    }
}
