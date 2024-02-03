package ro.deiutzentartainment.connection.handler.game;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzblaxo.cloud.fileutils.zip.FileUtils;
import ro.deiutzentartainment.config.ConfigFile;
import ro.deiutzentartainment.connection.ConnectionManager;
import ro.deiutzentartainment.connection.handler.ConnectionHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class PutGameDataHandler implements ConnectionHandler {
    private final ConnectionManager connectionManager;

    private static Logger _logger = LogManager.getLogger(PutGameDataHandler.class);

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;



    public PutGameDataHandler(ConnectionManager connectionManager, Socket socket, DataInputStream reader, DataOutputStream writer) {

        this.connectionManager = connectionManager;
        this.socket=socket;
        this.input=reader;
        this.output=writer;
    }

    @Override
    public void Start() {
        String name = getGameName();
        if(name.isBlank()){
            return;
        }
        File file = getFile(name,true,1);
        if(file.exists())
            FileUtils.delete(file);
        try {
            Files.receiveFile(input,SIZE_PACKET,file);
        } catch (Exception e) {
            _logger.error("Error receiving file",  e);
        }
        Stop();
    }

    public String getGameName(){
        try {
            String name = input.readUTF();

            _logger.debug("The name of the game is " + name);
            return name;
        } catch (IOException e) {
            throw new RuntimeException("Failed to get the name, the process needs to be restarted");
        }
    }

    @Override
    public void Stop() {
        try {
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
