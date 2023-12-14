package ro.deiutzentartainment;

import ro.deiutzentartainment.config.Config;
import ro.deiutzentartainment.config.ConfigFile;
import ro.deiutzentartainment.connection.ConnectionManager;

public class Main {

    private static int port;
    public static void main(String[] args) {

        port = (int) ConfigFile.instance().getConfig(Config.PORT);
        ConnectionManager connectionManager = new ConnectionManager(port);



    }
}