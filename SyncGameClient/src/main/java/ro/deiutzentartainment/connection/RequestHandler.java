package ro.deiutzentartainment.connection;

import lombok.SneakyThrows;
import ro.deiutzentartainment.connection.handler.game.GetGameDataHandler;
import ro.deiutzentartainment.connection.handler.save.GetGameSaveHandler;
import ro.deiutzentartainment.connection.handler.game.PutGameDataHandler;
import ro.deiutzentartainment.connection.handler.save.PutGameSaveHandler;
import ro.deiutzentartainment.games.data.Game;

import java.io.*;
import java.net.Socket;

public class RequestHandler {
    private final String hostname;
    private final int port;

    private boolean checkSize = true;

    public RequestHandler(String hostname, int port){
        this.hostname=hostname;
        this.port=port;
        System.out.println("hostname: " + hostname + " - Port is "+ port);
    }
    @SneakyThrows
    public void putGameSave(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        sendInitializationPacket(writer,0);
        PutGameSaveHandler gameRequestHandler = new PutGameSaveHandler(game,socket,writer,reader);
        gameRequestHandler.Start();
    }
    @SneakyThrows
    public void getGameSave(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        sendInitializationPacket(writer,1);
        GetGameSaveHandler gameRequestHandler = new GetGameSaveHandler(game,socket,reader,writer);
        gameRequestHandler.Start();
    }
    @SneakyThrows
    public void putGameData(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        sendInitializationPacket(writer,2);
        PutGameDataHandler gameRequestHandler = new PutGameDataHandler(game,socket,writer,reader);
        gameRequestHandler.Start();
    }
    @SneakyThrows
    public void getGameData(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        sendInitializationPacket(writer,3);
        GetGameDataHandler gameRequestHandler = new GetGameDataHandler(game,socket,reader,writer);
        gameRequestHandler.Start();
    }

    private void sendInitializationPacket(DataOutputStream printWriter, int id) throws IOException {
        System.out.println("sending initialization");
        printWriter.writeInt(id);
        printWriter.flush();

    }
}
