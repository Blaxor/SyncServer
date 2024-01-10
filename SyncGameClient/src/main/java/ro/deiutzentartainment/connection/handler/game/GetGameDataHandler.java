package ro.deiutzentartainment.connection.handler.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.connection.handler.Handler;
import ro.deiutzentartainment.connection.handler.save.GetGameSaveHandler;
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

    private static final Logger logger = LoggerFactory.getLogger(GetGameDataHandler.class);
    public GetGameDataHandler(Game game, Socket socket, DataInputStream reader, DataOutputStream writer) {
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
        if(existConfirmation()){

            try {
                Files.receiveFile(input,SIZE_PACKET,getTempFile());
                GameHelper.unpackGame(game,getTempFile());
            } catch (Exception e) {
                logger.error("Failed to get the file", e);
            }


        }else{
            logger.info("There is nothing stored for this game.");

        }
        Stop();

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

    public boolean existConfirmation(){
        try {
            return input.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public void Stop() {
        FileUtils.delete(getTempFile());
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
