package ro.deiutzentartainment.connection.handler;

import lombok.SneakyThrows;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigFile;
import ro.deiutzentartainment.connection.ConnectionManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PutGameConnectionHandler implements ConnectionHandler{

    Socket socket;
    ConnectionManager connectionManager;


    private UUID uuid;

    String gameName;

    private File folderTemp;

    private DataInputStream reader;
    private DataOutputStream writer;

    private static int size_packet = 1024*1024*200; // (200mb)





    public PutGameConnectionHandler(ConnectionManager connectionManager, Socket socket,DataInputStream reader, DataOutputStream writer, UUID uuid) {
        this.socket=socket;
        this.connectionManager=connectionManager;
        this.uuid =uuid;
        this.reader = reader;
        this.writer =writer;
        size_packet  = (int) ConfigFile.instance().getConfig(Config.PACKET_SIZE);
    }
    @Override
    public void Start() {

        generateTempFolder();
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

    private boolean isClientBigger() throws IOException {
        if(ConfigFile.instance().getBoolean(Config.CHECK_SIZE)){

            Long clientSize = reader.readLong();
            Long localsize = getSaveFile(gameName,true).length();
            System.out.println(clientSize +" <- client | local -> " + localsize);
            return clientSize >= localsize;
        }
        return true;
    }

    @Override
    public void Stop() {
        System.out.println("Closing PutGameConnectionHandler");
        deleteTempFolder();
        connectionManager.getConnections().remove(uuid);
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    boolean isLocalModificationNewer() throws IOException {
        long clientEpoch = reader.readLong();
        long localEpoch = generateSaveLocation(gameName).lastModified();
        System.out.println("Client epoch " + clientEpoch+ "| Local Epoch " + localEpoch );
        return localEpoch > clientEpoch;
    }
    boolean sendIsLocalModificationNewer() throws IOException {
        boolean isLocalNewer = isLocalModificationNewer();
        writer.writeBoolean(isLocalNewer);
        writer.flush();
        return isLocalNewer;
    }
    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void setTempFolder(File temp) {
        this.folderTemp= temp;
    }

    @Override
    public File getTempFolder() {
        return folderTemp;
    }

    @Override
    public int getPacketSize() {
        return size_packet;
    }

    @SneakyThrows
    public String readGameName(DataInputStream inputStream){
        System.out.println("Waiting for game name");
        return inputStream.readUTF();
    }

    public void readPackets(DataInputStream inputStream){

        try {
            if(generateSaveLocation(gameName).exists()){
                FileUtils.delete(generateSaveLocation(gameName));
            }
            Files.receiveFile(inputStream,getPacketSize(),generateSaveLocation(gameName));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private File generateSaveLocation(String gameName) {
        File folderSave = ConfigFile.instance().getSaveGameFolder();
        if(!folderSave.exists())
            folderSave.mkdirs();
        return new File(folderSave,gameName+".zip");


    }

}
//TODO ORDER PACKETS ->
// 1. Request Data (number of splits, type of request, Game name, verifyIntegrityNumber)
// 2...... splits; -> Split { Number of split, data (max 5mb) }




