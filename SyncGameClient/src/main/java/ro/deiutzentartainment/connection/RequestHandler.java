package ro.deiutzentartainment.connection;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import ro.deiutzentartainment.connection.handler.GetGameRequestHandler;
import ro.deiutzentartainment.connection.handler.PutGameRequestsHandler;
import ro.deiutzentartainment.games.data.Game;

import java.io.*;
import java.net.Socket;

public class RequestHandler {
    private final String hostname;
    private final int port;

    public RequestHandler(String hostname, int port){
        this.hostname=hostname;
        this.port=port;
        System.out.println("hostname: " + hostname + " - Port is "+ port);
    }
    @SneakyThrows
    public void getGameSave(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        sendInitializationPacket(writer,1);
        GetGameRequestHandler gameRequestHandler = new GetGameRequestHandler(game,socket,reader,writer);
        gameRequestHandler.Start();


    }
    @SneakyThrows
    public boolean putGameSave(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        sendInitializationPacket(writer,0);
        PutGameRequestsHandler gameRequestHandler = new PutGameRequestsHandler(game,socket,writer,reader);
        gameRequestHandler.Start();
        return false;
    }
    private void sendInitializationPacket(DataOutputStream printWriter, int id) throws IOException {
        printWriter.writeInt(id);
        printWriter.flush();

    }
}
