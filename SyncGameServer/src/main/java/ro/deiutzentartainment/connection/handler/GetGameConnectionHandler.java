package ro.deiutzentartainment.connection.handler;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import ro.deiutzentartainment.connection.ConnectionManager;

import ro.deiutzentartainment.fileutils.communication.Files;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;

public class GetGameConnectionHandler implements ConnectionHandler {
    ConnectionManager connectionManager;
    Socket socket;
    DataInputStream reader;
    DataOutputStream writer;
    UUID uuid;
    private File tempfile;
    private static int size_packet = 1024*1024*200; // (200mb)
    static Gson gson = new Gson();
    String gameName;

    public GetGameConnectionHandler(ConnectionManager connectionHandler, Socket socket, DataInputStream reader, DataOutputStream writer, UUID uuid) {
    this.connectionManager=connectionHandler;
    this.socket=socket;
    this.reader=reader;
    this.writer=writer;
    this.uuid=uuid;
    }
//TODO             //INITIALIZATION(INT), GameName(UTF), (data.....) bytes
    @SneakyThrows
    @Override
    public void Start() {

                try {
                    gameName = readGameName(reader);
                    generateTempFolder();
                    File file = getSaveFile(gameName, false);
                    sendAllThePackets(writer,file);
                    while(!socket.isClosed()){

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

    @SneakyThrows
    public void sendAllThePackets(DataOutputStream stream, File file){
        Files.sendFile(stream,file,16*1024);
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


}
