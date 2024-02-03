package ro.deiutzentartainment.connection.handler.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.connection.handler.Handler;
import ro.deiutzentartainment.exceptions.gamefile.InvalidNameException;
import ro.deiutzentartainment.games.data.Game;
import ro.deiutzentartainment.games.data.GameHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GetGameDataHandler implements Handler {

    private Game game;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;

    private static Logger _logger = LogManager.getLogger(GetGameDataHandler.class);
    public GetGameDataHandler(Game game, Socket socket, DataInputStream reader, DataOutputStream writer) {
        this.game = game;
        this.socket = socket;
        this.output = writer;
        this.input = reader;
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
        if (existConfirmation()) {
            try {
                receiveAllThePackets(input);
                GameHelper.unpackGame(game, getTempFile());
            } catch (Exception e) {
                _logger.error("Failed to get the file", e);
            }


        } else {
            _logger.warn("There is nothing stored for this game.");

        }
        Stop();

    }



    public void sendGameName() throws InvalidNameException {
        try {
            _logger.info("Sending the game name " + game.getName());
            output.writeUTF(game.getName());
            output.flush();
        } catch (IOException e) {
            throw new InvalidNameException(game.getName());
        }
    }

    public boolean existConfirmation() {
        try {

            return input.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receiveAllThePackets(DataInputStream bufferedReader) {
        try {
            _logger.info("Downloading the data.");
            Files.receiveFile(bufferedReader, SIZE_PACKET, getTempFile());
            _logger.info("The data has been downloaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Stop() {
            _logger.info("Finish Game Data downloading, deleting the temporary file.");
            FileUtils.delete(getTempFile());
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
}
