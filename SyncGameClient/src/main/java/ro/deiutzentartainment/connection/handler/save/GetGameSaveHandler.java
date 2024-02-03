package ro.deiutzentartainment.connection.handler.save;

import com.google.gson.Gson;
import net.lingala.zip4j.exception.ZipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.ArchiveHandler;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;
import ro.deiutzentartainment.connection.handler.Handler;
import ro.deiutzentartainment.games.data.Game;
import ro.deiutzentartainment.games.data.GameHelper;

import java.io.*;
import java.net.Socket;




public class GetGameSaveHandler implements Handler {

    Socket socket;
    DataInputStream bufferedReader;
    Game game;
    DataOutputStream printWriter;
    private static Logger _logger = LogManager.getLogger(GetGameSaveHandler.class);

    public GetGameSaveHandler(Game game, Socket socket,  DataInputStream bufferedReader, DataOutputStream printWriter){
        this.game=game;
        this.socket=socket;
        this.bufferedReader=bufferedReader;
        this.printWriter=printWriter;
    }


    @Override
    public void Start() {
        try {

            sendGameName(printWriter);
            //TODO CHECK IF ANYTHING EXIST ON SERVER, SEE @GetGameSaveHandler
            if(ConfigConnection.getInstance().getBoolean(Config.CHECK_SIZE)) {
                if (isClientBigger()) {
                    _logger.info("Client size is bigger, not loading the cloud sync game save.");
                } else {
                    _logger.info("Client size is not bigger, loading the cloud sync game save");
                    receiveAllThePackets(bufferedReader);
                    unzipToFile();
                }
            }else{
                receiveAllThePackets(bufferedReader);
                unzipToFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Stop();
        }
    }
    private boolean isClientBigger() throws IOException {
        if(ConfigConnection.getInstance().getBoolean(Config.CHECK_SIZE)){
            long size = getZipSize();
            printWriter.writeLong(size);
            printWriter.flush();
            return bufferedReader.readBoolean();
        }
        return bufferedReader.readBoolean();
    }

    private void unzipToFile() {
            GameHelper.unpackSave(game,getTempFile());

    }

    private void receiveAllThePackets(DataInputStream bufferedReader) {
        try {
            _logger.info("Downloading the data.");
            Files.receiveFile(bufferedReader,SIZE_PACKET,getTempFile());
            _logger.info("The data has been downloaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendGameName(DataOutputStream writer) throws IOException {
        _logger.info("Sending the game name " +game.getName() );
        writer.writeUTF(game.getName());
        writer.flush();
    }
    private long getZipSize() throws ZipException, FileNotFoundException {
        _logger.info("Creating zip file");
        File saveFile = new File(game.getSavePath());
        if(!saveFile.exists()) {
            _logger.info("Save file do not exist. Size is 0 then.");
            return 0;
        }

        ArchiveHandler.zip(game.getSavePath(),getTempFile().getAbsolutePath());
        _logger.info("Size of the zip is:  " + getTempFile().length());
        return getTempFile().length();

    }

    @Override
    public void Stop() {
        _logger.info("Finish Game Save downloading, deleting the temporary file.");
        FileUtils.delete(getTempFile());


    }


}
