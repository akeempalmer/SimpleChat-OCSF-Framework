package com.akeempalmer.server;

import com.akeempalmer.EchoServer;
import com.akeempalmer.common.ChatIF;

import java.io.IOException;

//public class ChatServer extends AbstractServer {

public class ChatServer extends ObservableServer {
    /**
     * The interface type variable. It allows the implementation of
     * the display method in the server.
     */
    EchoServer server;


    /**
     * Constructs an instance of the chat client.
     *
     * @param port     The port number to connect on.
     * @param server The interface type variable.
     */
    public ChatServer(int port, EchoServer server)
            throws IOException {
        super(port); // Call the superclass constructor
        this.server = server;

    }

    public void handleCommandsFromServerUI(String message) throws IOException {
        String cleansedMessage = message.replaceFirst("#", "");

        String[] commandArgMessage = cleansedMessage.split(" ");
        String command = commandArgMessage[0];
        String commandArg = "";

        if (commandArgMessage.length > 1)
            commandArg = commandArgMessage[1];

        switch(command) {
            case "quit":
                quit();
                break;
            case "stop":
                server.stopListening();
                break;
            case "close":
                server.close();
                break;
            case "setport":
                server.setPort(Integer.parseInt(commandArg));
                break;
            case "start":
                server.listen();
                break;
            case "getport":
                System.out.printf("The server is listening on port %s\n", server.getPort());
                break;
            default:
                System.out.println("Invalid command entered.");
                break;

        }
    }


    public void quit() {
        try {
            System.out.println("Quiting the system please wait...");
            server.close();
        } catch (IOException var2) {
        }

        System.exit(0);
    }

//    @Override
//    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
//
//    }


}

