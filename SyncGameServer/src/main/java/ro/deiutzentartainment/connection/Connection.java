package ro.deiutzentartainment.connection;

import com.google.gson.Gson;
import ro.deiutzentartainment.connection.handler.ConnectionHandler;
import ro.deiutzentartainment.connection.handler.GetGameConnectionHandler;
import ro.deiutzentartainment.connection.handler.PutGameConnectionHandler;

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
                connectionHandler = new PutGameConnectionHandler(connectionManager, socket, reader, writer, uuid);
                break;
            case 1:
                connectionHandler = new GetGameConnectionHandler(connectionManager,socket,reader,writer,uuid);
                break;
        }
        connectionHandler.Start();


    }
}