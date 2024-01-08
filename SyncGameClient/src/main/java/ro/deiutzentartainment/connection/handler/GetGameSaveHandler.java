package ro.deiutzentartainment.connection.handler;

import com.google.gson.Gson;
import net.lingala.zip4j.exception.ZipException;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.ArchiveHandler;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;
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
    private static Gson gson = new Gson();
    private File tempFolder;
    String dataExtension = ".zip";
    static int packet_size = 1024;

    public GetGameSaveHandler(Game game, Socket socket,  DataInputStream bufferedReader, DataOutputStream printWriter){
        this.game=game;
        this.socket=socket;
        this.bufferedReader=bufferedReader;
        this.printWriter=printWriter;
        this.packet_size= (int) ConfigConnection.getInstance().getConfig(Config.BATCH_SIZE);
    }


    @Override
    public void Start() {
        try {

            sendGameName(printWriter);
            generateTempFolder();
            if(isClientBigger()) {
                System.out.println("Client size is bigger, not loading the cloud sync game save.");
            }
            else {
                System.out.println("Client size is not bigger, loading the cloud sync game save");
                receiveAllThePackets(bufferedReader);
                unzipToFile();
                FileUtils.delete(new File(getZipTempPath()));
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
        return false;
    }

    private void unzipToFile() {

            System.out.println("Unzipping the game save " + game.getName() );
            GameHelper.unpackSave(game,new File(getZipTempPath()));
            GameHelper.unpackGame(game,new File(getZipTempPath()));
            System.out.println("Done unzipping the game save " + game.getName() );

    }

    private void receiveAllThePackets(DataInputStream bufferedReader) {
        try {
            Files.receiveFile(bufferedReader,getPacketSize(),new File(getZipTempPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getZipTempPath(){
        return tempFolder.getPath()+"/getTempZip"+dataExtension;
    }


    public void sendGameName(DataOutputStream writer) throws IOException {
        writer.writeUTF(game.getName());
        writer.flush();
    }
    @Override
    public void Stop() {
        System.out.println("Closing getGameRequestHandler");
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private long getZipSize() throws ZipException, FileNotFoundException {
        System.out.println("Creating zip file");
        File saveFile = new File(game.getSavePath());
        if(!saveFile.exists()) {
            System.out.println("Save file do not exist. Size is 0 then.");
            return 0;
        }

        ArchiveHandler.zip(game.getSavePath(),getZipTempPath());
        System.out.println("Size of the zip is:  " + new File(getZipTempPath()).length());
        return new File(getZipTempPath()).length();

    }

    @Override
    public File getTempFolder() {
        return tempFolder;
    }

    @Override
    public void setTempFolder(File file) {
tempFolder = file;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public int getPacketSize() {
        return packet_size;
    }


}
