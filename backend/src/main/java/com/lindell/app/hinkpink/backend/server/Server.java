package com.lindell.app.hinkpink.backend.server;

/**
 * Created by lindell on 6/1/15.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.lindell.app.hinkpink.backend.server.facade.ServerFacade;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

public class Server {

    private int server_port_number;
    private static final int MAX_WAITING_CONNECTIONS = 10;

    private static Logger logger;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println("Could not initialize log: " + e.getMessage());
        }
    }

    private static void initLog() throws IOException {

        Level logLevel = Level.INFO;

        logger = Logger.getLogger("record_server");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);

        FileHandler fileHandler = new FileHandler("log.txt", false);
        fileHandler.setLevel(logLevel);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

    private HttpServer server;

    private Server(int server_port_number) {
        this.server_port_number = server_port_number;
        return;
    }

    private void run() {

        logger.info("Initializing Model");
        ServerFacade.initialize();

        logger.info("Initializing HTTP Server");

        try {
            server = HttpServer.create(new InetSocketAddress(server_port_number),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        server.setExecutor(null); // use the default executor

        validateUserHandler = new ValidateUserHandler();

        server.createContext("/ValidateUser", validateUserHandler);

        logger.info("Starting HTTP Server");

        server.start();
    }

    private HttpHandler validateUserHandler;

    public static void main(String[] args) {
        new Server(Integer.parseInt(args[0])).run();
    }

}
