package ro.deiutzentartainment.connection;

import com.google.gson.Gson;
import ro.deiutzentartainment.connection.handler.ConnectionHandler;
import ro.deiutzentartainment.connection.handler.game.GetGameDataHandler;
import ro.deiutzentartainment.connection.handler.save.GetGameSaveHandler;
import ro.deiutzentartainment.connection.handler.save.PutGameSaveHandler;
import ro.deiutzentartainment.connection.handler.game.PutGameDataHandler;

import java.io.*;
import java.net.Socket;
import java.util.UUID;


public class Connection {

    ConnectionManager connectionManager;
    ConnectionHandler connectionHandler;
    Socket socket;
    UUID uuid;

    DataInputStream reader;
    DataOutputStream writer;

    static Gson gson = new Gson();


    public Connection(ConnectionManager connectionManager, Socket socket, UUID uuid) throws IOException {
        this.socket = socket;
        this.uuid = uuid;
        this.connectionManager = connectionManager;
        try {
             reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
             writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int type = reader.readInt();
        System.out.println("Reading Connection Initialization packet " + type);

        switch (type) {
            case 0:
                connectionHandler = new PutGameSaveHandler(connectionManager, socket, reader, writer);
                break;
            case 1:
                connectionHandler = new GetGameSaveHandler(connectionManager,socket,reader,writer);
                break;
            case 2:
                connectionHandler = new PutGameDataHandler(connectionManager,socket,reader,writer);
                break;
            case 3:
                connectionHandler = new GetGameDataHandler(connectionManager,socket,reader,writer);
                break;
        }
        connectionHandler.Start();


    }
}