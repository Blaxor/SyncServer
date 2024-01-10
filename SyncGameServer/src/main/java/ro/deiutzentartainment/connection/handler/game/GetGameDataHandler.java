package ro.deiutzentartainment.connection.handler.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.deiutzblaxo.cloud.fileutils.communication.Files;
import ro.deiutzentartainment.connection.ConnectionManager;
import ro.deiutzentartainment.connection.handler.ConnectionHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GetGameDataHandler implements ConnectionHandler {

    private final ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(PutGameDataHandler.class);
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;

    public GetGameDataHandler(ConnectionManager connectionManager, Socket socket, DataInputStream reader, DataOutputStream writer) {

        this.connectionManager = connectionManager;
        this.socket=socket;
        this.input=reader;
        this.output=writer;
    }

    @Override
    public void Start() {

        String name = getGameName();
        boolean exist = checkExist(name);
        try{
                output.writeBoolean(exist);
                output.flush();
                if(exist)
                    Files.sendFile(output, getFile(name, false, 1), SIZE_PACKET);

            }catch (IOException e){
                e.printStackTrace();
            }

    }
    public String getGameName(){
        try {
            String name = input.readUTF();
            logger.debug("The name of the game is " + name);
            return name;
        } catch (IOException e) {
            throw new RuntimeException("Failed to get the name, the process needs to be restarted");
        }
    }
    public boolean checkExist(String name){
        return getFile(name,false,1).exists();
    }




    @Override
    public void Stop() {

    }
}
