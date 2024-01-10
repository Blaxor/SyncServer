package ro.deiutzentartainment.connection.handler.save;

import lombok.SneakyThrows;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigFile;
import ro.deiutzentartainment.connection.ConnectionManager;
import ro.deiutzentartainment.connection.handler.ConnectionHandler;


import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class GetGameSaveHandler implements ConnectionHandler {
    private ConnectionManager connectionManager;
    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
    private String gameName;




    public GetGameSaveHandler(ConnectionManager connectionHandler, Socket socket, DataInputStream reader, DataOutputStream writer) {
    this.connectionManager=connectionHandler;
    this.socket=socket;
    this.reader=reader;
    this.writer=writer;

    }
//TODO             //INITIALIZATION(INT), GameName(UTF), (data.....) bytes
    @SneakyThrows
    @Override
    public void Start() {

                try {
                    gameName = readGameName();
                    File file = getFile(gameName,false,0);
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
    private String readGameName(){
        return reader.readUTF();
    }

    private boolean isClientBigger() throws IOException {
        if(ConfigFile.instance().getBoolean(Config.CHECK_SIZE)){
            Long clientSize = reader.readLong();
            Long localsize = getFile(gameName,false,0).length();
            System.out.println(clientSize +" <- client | local -> " + localsize);

            return clientSize >= localsize;
        }
        return false;
    }

    @SneakyThrows
    public void sendAllThePackets(DataOutputStream stream, File file){
        Files.sendFile(stream,file,SIZE_PACKET);
        stream.flush();
        stream.close();
    }

    @Override
    public void Stop() {
        System.out.println("Closing getGameConnectionHandler");
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }




}
