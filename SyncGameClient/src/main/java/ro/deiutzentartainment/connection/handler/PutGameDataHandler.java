package ro.deiutzentartainment.connection.handler;

import ro.deiutzentartainment.games.data.Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;

public class PutGameDataHandler implements Handler {
    public PutGameDataHandler(Game game, Socket socket, DataOutputStream writer, DataInputStream reader) {
    }

    @Override
    public void Start() {
        
    }

    @Override
    public void Stop() {

    }

    @Override
    public File getTempFolder() {
        return null;
    }

    @Override
    public void setTempFolder(File file) {

    }

    @Override
    public Game getGame() {
        return null;
    }

    @Override
    public int getPacketSize() {
        return 0;
    }
}
