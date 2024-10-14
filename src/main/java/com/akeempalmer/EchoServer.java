package com.akeempalmer;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Arrays;

import com.akeempalmer.client.ChatClient;
import com.akeempalmer.common.ChatIF;
import com.akeempalmer.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer implements ChatIF {
    // Class variables *************************************************

    /**
     * The default port to listen on.
     */
    final public static int DEFAULT_PORT = 5555;

    ChatServer server;

    // Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to connect on.
     */
    public EchoServer(int port) {
        super(port);
        try {
            server = new ChatServer(port, this);
        } catch (IOException exception) {
            System.out.println("Error: Can't setup connection!"
                    + " Terminating client.");
            System.exit(1);
        }
    }

    // Instance methods ************************************************

    /**
     * This method handles any messages received from the client.
     *
     * @param msg    The message received from the client.
     * @param client The connection from which the message originated.
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client);
        this.sendToAllClients(msg);
    }

    /**
     * This method overrides the one in the superclass. Called
     * when the server starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    /**
     * This method overrides the one in the superclass. Called
     * when the server stops listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }

    /**
     * This method overrides the one in the superclass. Called when a client
     * connects to the server.
     * @param client the connection connected to the client.
     */
    protected void clientConnected(ConnectionToClient client) {
        String host = client.toString();
        System.out.printf("A new client has connected to the server from %s\n", host);
    }

    /**
     * This method overrides the one in the superclass. Called when
     * the socket is no longer listening to the client. (EOF)
     * @param client    the client that raised the exception.
     * @param exception
     */
    protected synchronized void clientException(
            ConnectionToClient client, Throwable exception) {

        if (exception instanceof EOFException) {
            clientDisconnected(client);
        }
    }

    /**
     * This method overrides the one in the superclass. Called when a client
     * disconnects from the server.
     * @param client the connection connected to the client.
     */
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        System.out.println("A client has disconnected from the server.");
    }

    // Class methods ***************************************************
    /**
     * This method is responsible for the creation of
     * the server instance (there is no UI in this phase).
     *
     * @param args[0] The port number to listen on. Defaults to 5555
     *                if no argument is entered.
     */
    public static void main(String[] args) {
        int port = 0; // Port to listen on

        try {
            port = Integer.parseInt(args[0]); // Get port from command line
        } catch (Throwable t) {
            port = DEFAULT_PORT; // Set port to 5555
        }

        EchoServer sv = new EchoServer(port);

        try {
            sv.listen(); // Start listening for connections
            Thread.sleep(2 * 1000);
            sv.accept();

        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }

    /**
     * This method waits for input from the console. Once it is
     * received, it sends it to the client's message handler.
     */
    public void accept() {
        try {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true) {
                message = fromConsole.readLine();

                if (message.charAt(0) != '#') {
                    // Broadcast the message to all the connected clients
                    display(message);
                } else {
                    server.handleCommandsFromServerUI(message);
                }
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    @Override
    public void display(String message) {
        this.sendToAllClients("SERVER msg> " + message);
        System.out.println("SERVER msg> " + message);
    }
}
// End of EchoServer class