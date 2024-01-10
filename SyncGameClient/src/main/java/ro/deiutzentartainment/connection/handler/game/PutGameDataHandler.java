package ro.deiutzentartainment.connection.handler.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.connection.handler.Handler;
import ro.deiutzentartainment.exceptions.gamefile.InvalidNameException;
import ro.deiutzentartainment.games.data.Game;
import ro.deiutzentartainment.games.data.GameHelper;

import java.io.*;
import java.net.Socket;

public class PutGameDataHandler implements Handler {

    private Game game;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;

    private static final Logger logger = LoggerFactory.getLogger(PutGameDataHandler.class);

    public PutGameDataHandler(Game game, Socket socket, DataOutputStream writer, DataInputStream reader) {
        this.game=game;
        this.socket=socket;
        this.output=writer;
        this.input=reader;

    }

    @Override
    public void Start() {

        try {
            sendGameName();
        } catch (InvalidNameException e) {
            logger.info("Invalid Game name");
            Stop();
            return;
        }
        zipToTemp();
        try {
            logger.info("Sending file");
            Files.sendFile(output,getTempFile(),SIZE_PACKET);
        } catch (IOException e) {
            logger.error("Unable to send the file. Please retry",e);
        }
        Stop();
    }
    private void zipToTemp() {
        logger.info("Zipping the game");
        GameHelper.packGame(game,getTempFile());
        logger.info("Done zipping");

    }

    public void sendGameName() throws InvalidNameException {
        try {
            logger.debug("Sending the game name " + game.getName());
            output.writeUTF(game.getName());
            output.flush();
        } catch (IOException e) {
            logger.error("Unable to send the game name. Please retry.",e);
            throw new InvalidNameException(game.getName());
        }
    }


    @Override
    public void Stop() {
        FileUtils.delete(getTempFile());
    }
}
