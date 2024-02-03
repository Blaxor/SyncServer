package ro.deiutzentartainment.connection.handler.save;

import net.lingala.zip4j.exception.ZipException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.ArchiveHandler;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;
import ro.deiutzentartainment.config.ConfigFiles;
import ro.deiutzentartainment.connection.handler.Handler;
import ro.deiutzentartainment.games.GameHandler;
import ro.deiutzentartainment.games.data.Game;
import ro.deiutzentartainment.games.data.GameHelper;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PutGameSaveHandler implements Handler {



//TODO             //INITIALIZATION(INT), GameName(UTF), (data.....) bytes

    Socket socket;
    DataOutputStream output;
    DataInputStream input;
    Game game;
    private static Logger _logger = LogManager.getLogger(PutGameSaveHandler.class);
    public PutGameSaveHandler(Game game, Socket socket, DataOutputStream output, DataInputStream input){
        this.socket=socket;
        this.game=game;
        this.input=input;
        this.output=output;
    }
    @Override
    public void Start() {
        try {
            sendGameName(output);

            sendLastModificationTime(new File(game.getSavePath()));

            if(getConfirmationLocalIsOlder()) {
                _logger.info("Local is Older. Stopping process");
                return;
            }else{
                _logger.info("Local is newer, Continue");
            }
            zipToTemp();
            if(ConfigConnection.getInstance().getBoolean(Config.CHECK_SIZE)) {
                if (isClientBigger()) {
                    _logger.info("Client size is bigger,saving.");
                    sendPackets(output);
                } else {
                    _logger.info("Client size is not bigger, not saving");
                }
            }else{
                sendPackets(output);
            }

        }catch (FileNotFoundException nf){
            _logger.info("Game save not found");
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            _logger.info("STOP PutGameRequestHandler");
            Stop();
        }
    }
    private boolean isClientBigger() throws IOException {
        if(ConfigConnection.getInstance().getBoolean(Config.CHECK_SIZE)){
            long size = getTempFile().length();
            output.writeLong(size);
            output.flush();
            return input.readBoolean();
        }

        return input.readBoolean();
    }


    private void zipToTemp() throws FileNotFoundException {
        GameHelper.packSave(game,getTempFile());
    }
    public void sendLastModificationTime(File file) throws IOException {
        long last = FileUtils.lastTimeModificationTime(file);
        _logger.info("Last modification time epoch in ms: " + last + " in date second " +
                LocalDateTime.ofEpochSecond(last/1000,0,ZoneOffset.UTC) + " sending it.");
        output.writeLong(last);
        output.flush();
    }
    public boolean getConfirmationLocalIsOlder() throws IOException {
        return input.readBoolean();
    }

    public void sendGameName(DataOutputStream output) throws IOException {
        _logger.info("Sending the game name " +game.getName() );
        output.writeUTF(game.getName());
        output.flush();
    }

    public void sendPackets(DataOutputStream output) throws IOException {
        _logger.info("Starting sending the packets (packet_size= " + SIZE_PACKET+")" );
        Files.sendFile(output,getTempFile(),SIZE_PACKET);
        output.flush();
        _logger.info("The packets have been sent.");
    }

    @Override
    public void Stop() {
        _logger.info("Finish Game Save saving, deleting the temporary file.");
        FileUtils.delete(getTempFile());

    }





}
