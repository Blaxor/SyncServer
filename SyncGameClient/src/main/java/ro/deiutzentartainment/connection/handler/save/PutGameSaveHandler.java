package ro.deiutzentartainment.connection.handler.save;

import net.lingala.zip4j.exception.ZipException;

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
                System.out.println("Local is Older. Stopping process");
                return;
            }else{
                System.out.println("Local is newer, continue sending data");
            }
            zipToTemp();
            if(isClientBigger()) {
                System.out.println("Client size is bigger,saving .");
                sendPackets(output);
            }
            else {
                System.out.println("Client size is not bigger, not saving");
            }

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
            long size = getTempFile().length();
            output.writeLong(size);
            output.flush();
            return input.readBoolean();
        }

        return input.readBoolean();
    }


    private void zipToTemp() throws FileNotFoundException {
        System.out.println("Creating zip file");
        GameHelper.packSave(game,getTempFile());
        System.out.println("Created the zip file");
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
        System.out.println("Sending file");
        Files.sendFile(output,getTempFile(),SIZE_PACKET);
        System.out.println(" file sent");
        output.flush();
       //output.close();
    }

    @Override
    public void Stop() {
        System.out.println("Closing putGameRequestHandler");
        FileUtils.delete(getTempFile());

    }





}
