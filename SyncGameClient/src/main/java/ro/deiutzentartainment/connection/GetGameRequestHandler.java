package ro.deiutzentartainment.connection;

import com.google.gson.Gson;
import net.lingala.zip4j.exception.ZipException;
import ro.deiutzentartainment.fileutils.zip.FileUtils;
import ro.deiutzentartainment.fileutils.communication.Files;
import ro.deiutzentartainment.games.data.Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

//TODO NEW ORDER OF REQUESTS -> NAME_GAME(HEAD??) -> PACKETS(ReadFileInBatch.class) -> FINAL PACKET(ReadFileInBatch.class) -> CONFIRMATION DOWNLOAD



public class GetGameRequestHandler implements Handler {

    Socket socket;
    DataInputStream bufferedReader;
    Game game;
    DataOutputStream printWriter;
    private static Gson gson = new Gson();
    private File tempFolder;
    String dataExtension = ".zip";


    public GetGameRequestHandler(Game game, Socket socket,  DataInputStream bufferedReader, DataOutputStream printWriter){
        this.game=game;
        this.socket=socket;
        this.bufferedReader=bufferedReader;
        this.printWriter=printWriter;
    }


    @Override
    public void Start() {
        try {
            //INITIALIZATION(INT), GameName(UTF), (data.....) bytes

            sendGameName(printWriter);
            generateTempFolder();
            receiveAllThePackets(bufferedReader);
            unzipToFile();
            FileUtils.delete(new File(getZipTempPath()));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Stop();
        }
    }

    private void unzipToFile() {
        try {
            System.out.println("Unzipping the game save " + game.getName() );
            FileUtils.ArchiveHandler.unzip(getZipTempPath(),game.getSavePath());
            System.out.println("Done unzipping the game save " + game.getName() );
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private void receiveAllThePackets(DataInputStream bufferedReader) {
        try {
            Files.receiveFile(bufferedReader,16*1024,new File(getZipTempPath()));
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




}
