package ro.deiutzentartainment.connection;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.deiutzentartainment.connection.handler.game.GetGameDataHandler;
import ro.deiutzentartainment.connection.handler.save.GetGameSaveHandler;
import ro.deiutzentartainment.connection.handler.game.PutGameDataHandler;
import ro.deiutzentartainment.connection.handler.save.PutGameSaveHandler;
import ro.deiutzentartainment.games.data.Game;

import java.io.*;
import java.net.Socket;

public class RequestHandler {
    private final String hostname;

    private static Logger _logger = LogManager.getLogger(RequestHandler.class);
    private final int port;

    private boolean checkSize = true;

    public RequestHandler(String hostname, int port){
        this.hostname=hostname;
        this.port=port;
        _logger.info("The connection information is: hostname: " + hostname + " and port is "+ port);
    }
    @SneakyThrows
    public void putGameSave(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        _logger.info("Connection has been created");
        sendInitializationPacket(writer,0);
        PutGameSaveHandler gameRequestHandler = new PutGameSaveHandler(game,socket,writer,reader);
        gameRequestHandler.Start();
    }
    @SneakyThrows
    public void getGameSave(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        _logger.info("Connection has been created");
        sendInitializationPacket(writer,1);
        GetGameSaveHandler gameRequestHandler = new GetGameSaveHandler(game,socket,reader,writer);
        gameRequestHandler.Start();
    }
    @SneakyThrows
    public void putGameData(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        _logger.info("Connection has been created");
        sendInitializationPacket(writer,2);
        PutGameDataHandler gameRequestHandler = new PutGameDataHandler(game,socket,writer,reader);
        gameRequestHandler.Start();
    }
    @SneakyThrows
    public void getGameData(Game game){
        Socket socket = new Socket(hostname,port);
        DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        _logger.info("Connection has been created");
        sendInitializationPacket(writer,3);
        GetGameDataHandler gameRequestHandler = new GetGameDataHandler(game,socket,reader,writer);
        gameRequestHandler.Start();
    }

    private void sendInitializationPacket(DataOutputStream printWriter, int id) throws IOException {
        _logger.debug("Sending the initialization packet("+ id+")");
        printWriter.writeInt(id);
        printWriter.flush();
        _logger.debug("Initialization packet has been sent(" + id+")");

    }
}
