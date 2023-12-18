package ro.deiutzentartainment.connection.handler;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigFile;
import ro.deiutzentartainment.connection.ConnectionManager;


import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class GetGameConnectionHandler implements ConnectionHandler {
    ConnectionManager connectionManager;
    Socket socket;
    DataInputStream reader;
    DataOutputStream writer;
    UUID uuid;
    private File tempfile;
    private static int size_packet = 1024*1024*200; // (200mb)
    String gameName;

    public GetGameConnectionHandler(ConnectionManager connectionHandler, Socket socket, DataInputStream reader, DataOutputStream writer, UUID uuid) {
    this.connectionManager=connectionHandler;
    this.socket=socket;
    this.reader=reader;
    this.writer=writer;
    this.uuid=uuid;
    size_packet = (int) ConfigFile.instance().getConfig(Config.PACKET_SIZE);
    }
//TODO             //INITIALIZATION(INT), GameName(UTF), (data.....) bytes
    @SneakyThrows
    @Override
    public void Start() {

                try {
                    gameName = readGameName(reader);
                    generateTempFolder();
                    File file = getSaveFile(gameName, false);
                    if(isClientBigger()) {
                        System.out.println("Client size is bigger, not sending the game");
                        writer.writeBoolean(true);
                        writer.flush();
                    }
                    else {
                        System.out.println("Client size is not bigger, sending the game");
                        writer.writeBoolean(false);
                        writer.flush();
                        sendAllThePackets(writer, file);
                    }


                }catch (Exception e){
                    e.printStackTrace();

                }finally {

                    Stop();
                }
    }

    @SneakyThrows
    private String readGameName(DataInputStream reader){
        return reader.readUTF();
    }

    private boolean isClientBigger() throws IOException {
        if(ConfigFile.instance().getBoolean(Config.CHECK_SIZE)){
            Long clientSize = reader.readLong();
            Long localsize = getSaveFile(gameName,false).length();
            System.out.println(clientSize +" <- client | local -> " + localsize);

            return clientSize >= localsize;
        }
        return false;
    }

    @SneakyThrows
    public void sendAllThePackets(DataOutputStream stream, File file){
        Files.sendFile(stream,file,getPacketSize());
        stream.flush();
        stream.close();
    }

    @Override
    public void Stop() {
        System.out.println("Closing getGameConnectionHandler");
        deleteTempFolder();
        connectionManager.getConnections().remove(uuid);
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void setTempFolder(File temp) {
        this.tempfile= temp;
    }

    @Override
    public File getTempFolder() {
return tempfile;
    }

    @Override
    public int getPacketSize() {
        return size_packet;
    }


}
