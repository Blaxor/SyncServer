package ro.deiutzentartainment.connection.handler.save;

import com.google.gson.Gson;
import net.lingala.zip4j.exception.ZipException;
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

//TODO NEW ORDER OF REQUESTS -> NAME_GAME(HEAD??) -> PACKETS(ReadFileInBatch.class) -> FINAL PACKET(ReadFileInBatch.class) -> CONFIRMATION DOWNLOAD



public class GetGameSaveHandler implements Handler {

    Socket socket;
    DataInputStream bufferedReader;
    Game game;
    DataOutputStream printWriter;
    private File tempFolder;

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
            if(isClientBigger()) {
                System.out.println("Client size is bigger, not loading the cloud sync game save.");
            }
            else {
                System.out.println("Client size is not bigger, loading the cloud sync game save");
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
            System.out.println("Unzipping the game save " + game.getName() );
            GameHelper.unpackSave(game,getTempFile());
            System.out.println("Done unzipping the game save " + game.getName() );

    }

    private void receiveAllThePackets(DataInputStream bufferedReader) {
        try {
            Files.receiveFile(bufferedReader,SIZE_PACKET,getTempFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendGameName(DataOutputStream writer) throws IOException {
        writer.writeUTF(game.getName());
        writer.flush();
    }
    private long getZipSize() throws ZipException, FileNotFoundException {
        System.out.println("Creating zip file");
        File saveFile = new File(game.getSavePath());
        if(!saveFile.exists()) {
            System.out.println("Save file do not exist. Size is 0 then.");
            return 0;
        }

        ArchiveHandler.zip(game.getSavePath(),getTempFile().getAbsolutePath());
        System.out.println("Size of the zip is:  " + getTempFile().length());
        return getTempFile().length();

    }

    @Override
    public void Stop() {
        FileUtils.delete(getTempFile());
        System.out.println("Closing getGameRequestHandler");


    }


}
