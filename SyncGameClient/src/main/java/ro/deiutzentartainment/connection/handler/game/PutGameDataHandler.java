package ro.deiutzentartainment.connection.handler.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger _logger = LogManager.getLogger(PutGameDataHandler.class);

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
            _logger.info("Invalid Game name");
            Stop();
            return;
        }
        zipToTemp();
        try {
            sendPackets(output);
        } catch (IOException e) {
            _logger.error("Unable to send the file. Please retry",e);
        }
        Stop();
    }
    private void zipToTemp() {
        GameHelper.packGame(game,getTempFile());

    }
    public void sendPackets(DataOutputStream output) throws IOException {
        _logger.info("Starting sending the packets (packet_size= " + SIZE_PACKET+")" );
        Files.sendFile(output,getTempFile(),SIZE_PACKET);
        output.flush();
        _logger.info("The packets have been sent.");
    }

    public void sendGameName() throws InvalidNameException {
        try {
            _logger.info("Sending the game name " + game.getName());
            output.writeUTF(game.getName());
            output.flush();
        } catch (IOException e) {
            _logger.error("Unable to send the game name. Please retry.",e);
            throw new InvalidNameException(game.getName());
        }
    }


    @Override
    public void Stop() {
        _logger.info("Finish Game Data saving, deleting the temporary file.");

        FileUtils.delete(getTempFile());
    }
}
