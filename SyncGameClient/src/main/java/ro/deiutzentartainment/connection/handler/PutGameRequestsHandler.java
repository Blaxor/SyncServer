package ro.deiutzentartainment.connection.handler;

import net.lingala.zip4j.exception.ZipException;

import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.ArchiveHandler;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigConnection;
import ro.deiutzentartainment.config.ConfigFiles;
import ro.deiutzentartainment.connection.handler.Handler;
import ro.deiutzentartainment.games.data.Game;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PutGameRequestsHandler implements Handler {



//TODO             //INITIALIZATION(INT), GameName(UTF), (data.....) bytes

    Socket socket;
    DataOutputStream output;
    DataInputStream input;
    File tempFolder;
    Game game;

    static String dataExtension = ".zip";

    static int packet_size = 1024;
    public PutGameRequestsHandler(Game game, Socket socket, DataOutputStream output, DataInputStream input){
        this.socket=socket;
        this.game=game;
        this.input=input;
        this.output=output;
        this. packet_size= (int)ConfigConnection.getInstance().getConfig(Config.BATCH_SIZE);
    }
    @Override
    public void Start() {
        try {
            sendGameName(output);

            sendLastModificationTime(new File(game.getSavePath()));

            if(getConfirmationLocalIsOlder()) {
                System.out.println("Local is Older. Stopping process");
                return;
            }else{
                System.out.println("Local is newer, continue sending data");
            }
            generateTempFolder();
            createTempFile();
            if(isClientBigger()) {
                System.out.println("Client size is bigger,saving .");
                sendPackets(output);
            }
            else {
                System.out.println("Client size is not bigger, not saving");
            }



            FileUtils.delete(new File(getZipTempPath()));

        }catch (FileNotFoundException nf){
            System.out.println("Game save not found");
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("STOP PutGameRequestHandler");
            Stop();
        }
    }
    private boolean isClientBigger() throws IOException {
        if(ConfigConnection.getInstance().getBoolean(Config.CHECK_SIZE)){
            long size = new File(getZipTempPath()).length();
            output.writeLong(size);
            output.flush();
            return input.readBoolean();
        }
        return true;
    }


    private void createTempFile() throws ZipException, FileNotFoundException {
        System.out.println("Creating zip file");
        ArchiveHandler.zip(game.getSavePath(),getZipTempPath());
        System.out.println("Finished creating zip file");
    }
    public void sendLastModificationTime(File file) throws IOException {
        long last = FileUtils.lastTimeModificationTime(file);
        System.out.println("Last modification time epoch in ms: " + last + " in date second " +
                LocalDateTime.ofEpochSecond(last/1000,0,ZoneOffset.UTC));
        output.writeLong(last);
        output.flush();
    }
    public boolean getConfirmationLocalIsOlder() throws IOException {

        return input.readBoolean();

    }

    public void sendGameName(DataOutputStream output) throws IOException {
        output.writeUTF(game.getName());
        output.flush();
        System.out.println("Sending game name");
    }

    public void sendPackets(DataOutputStream output) throws IOException {
        Files.sendFile(output,new File(getZipTempPath()),getPacketSize());
        output.flush();
        output.close();
    }

    @Override
    public void Stop() {
        System.out.println("Closing putGameRequestHandler");
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public File getTempFolder() {
        return  tempFolder;
    }
    public String getZipTempPath(){
        return tempFolder.getPath()+"/putTempFile"+dataExtension;
    }

    @Override
    public void setTempFolder(File file) {
tempFolder = file;
    }

    @Override
    public Game getGame() {
        return null;
    }

    @Override
    public int getPacketSize() {
        return packet_size;
    }
}
