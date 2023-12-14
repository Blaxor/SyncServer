package ro.deiutzentartainment.connection;

import net.lingala.zip4j.exception.ZipException;
import ro.deiutzentartainment.fileutils.zip.FileUtils;
import ro.deiutzentartainment.fileutils.communication.Files;
import ro.deiutzentartainment.games.data.Game;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PutGameRequestsHandler implements Handler{



//TODO             //INITIALIZATION(INT), GameName(UTF), (data.....) bytes

    Socket socket;
    DataOutputStream output;
    DataInputStream input;
    File tempFolder;
    Game game;

    static String dataExtension = ".zip";

    public PutGameRequestsHandler(Game game, Socket socket, DataOutputStream output, DataInputStream input){
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
                System.out.println("Local is Older. Stopping process");
                return;
            }else{
                System.out.println("Local is newer, continue sending data");
            }
            generateTempFolder();
            createTempFile();
            sendPackets(output);
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

    private void createTempFile() throws ZipException, FileNotFoundException {
        System.out.println("Creating zip file");
        FileUtils.ArchiveHandler.zip(game.getSavePath(),getZipTempPath());
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
        Files.sendFile(output,new File(getZipTempPath()),16*1024);
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
}
