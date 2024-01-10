package ro.deiutzentartainment.connection.handler.save;

import lombok.SneakyThrows;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigFile;
import ro.deiutzentartainment.connection.ConnectionManager;
import ro.deiutzentartainment.connection.handler.ConnectionHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PutGameSaveHandler implements ConnectionHandler {

    private Socket socket;
    private ConnectionManager connectionManager;



    private String gameName;


    private DataInputStream reader;
    private DataOutputStream writer;

    public PutGameSaveHandler(ConnectionManager connectionManager, Socket socket, DataInputStream reader, DataOutputStream writer) {
        this.socket=socket;
        this.connectionManager=connectionManager;

        this.reader = reader;
        this.writer =writer;

    }
    @Override
    public void Start() {

        try {
            gameName = readGameName(reader);
                if(sendIsLocalModificationNewer()){
                    System.out.println("Local is newer. Stopping process");
                    return;
                }else{
                    System.out.println("Local is older. Continue");
                }

            if(isClientBigger()) {
                writer.writeBoolean(true);
                writer.flush();
                System.out.println("Client size is bigger, saving the client size.");
                readPackets(reader);
            }
            else {
                writer.writeBoolean(false);
                writer.flush();
                System.out.println("Client size is not bigger, not saving");
            }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "FAILED");
            }finally {
            Stop();
            }


    }

    @SneakyThrows
    public String readGameName(DataInputStream inputStream){
        System.out.println("Waiting for game name");
        return inputStream.readUTF();
    }

    boolean isLocalModificationNewer() throws IOException {
        long clientEpoch = reader.readLong();
        long localEpoch = getFile(gameName,false,0).lastModified();
        System.out.println("Client epoch " + clientEpoch+ "| Local Epoch " + localEpoch );
        return localEpoch > clientEpoch;
    }
    boolean sendIsLocalModificationNewer() throws IOException {
        boolean isLocalNewer = isLocalModificationNewer();
        writer.writeBoolean(isLocalNewer);
        writer.flush();
        return isLocalNewer;
    }

    private boolean isClientBigger() throws IOException {
        if(ConfigFile.instance().getBoolean(Config.CHECK_SIZE)){

            Long clientSize = reader.readLong();
            Long localsize = getFile(gameName,true,0).length();
            System.out.println(clientSize +" <- client | local -> " + localsize);
            return clientSize >= localsize;
        }
        return true;
    }

    public void readPackets(DataInputStream inputStream){

        try {
            File file = getFile(gameName,true,0);
            if(file.exists()){
                FileUtils.delete(file);
            }
            Files.receiveFile(inputStream,SIZE_PACKET,file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void Stop() {
        System.out.println("Closing PutGameConnectionHandler");
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
//TODO ORDER PACKETS ->
// 1. Request Data (number of splits, type of request, Game name, verifyIntegrityNumber)
// 2...... splits; -> Split { Number of split, data (max 5mb) }




