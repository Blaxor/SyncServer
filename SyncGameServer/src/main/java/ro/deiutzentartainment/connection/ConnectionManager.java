package ro.deiutzentartainment.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class ConnectionManager {


    private Thread listenerThread;
    public ConnectionManager(int port){
        listenerThread = new Thread(()->{
            try {
                ServerSocket serverSocket = new ServerSocket(port);

                while(true){
                    System.out.println("Waiting for new connection");
                    Socket socket = serverSocket.accept();
                    UUID uuid = UUID.randomUUID();
                    System.out.println("Server connection created with id " + uuid.toString());
                   new Connection(this,socket,uuid);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        });
        listenerThread.setName("Listener-Thread");
        listenerThread.start();

    }


}
