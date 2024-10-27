// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package com.akeempalmer.client;

import com.akeempalmer.common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
    // Instance variables **********************************************

    /**
     * The interface type variable. It allows the implementation of
     * the display method in the client.
     */
    ChatIF clientUI;
    String loginID;

    // Constructors ****************************************************

    /**
     * Constructs an instance of the chat client.
     *
     * @param loginID  The client user login ID.
     * @param host     The server to connect to.
     * @param port     The port number to connect on.
     * @param clientUI The interface type variable.
     */

    public ChatClient(String loginID, String host, int port, ChatIF clientUI)
            throws IOException {
        super(host, port); // Call the superclass constructor
        this.clientUI = clientUI;
        this.loginID = loginID;
        openConnection();
//        sendToServer("#login " + loginID);
    }

    // Instance methods ************************************************

    /**
     * This method handles all data that comes in from the server.
     *
     * @param msg The message from the server.
     */
    public void handleMessageFromServer(Object msg) {
        clientUI.display(msg.toString());
    }

    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromClientUI(String message) {
        try {
            String commandMessage = message.trim();
            if (commandMessage.charAt(0) == '#') {
                handleCommandsFromClientUI(message);
            } else {
                sendToServer(message);
            }
        } catch (IOException e) {
            clientUI.display("Could not send message to server.  Terminating client.");
            quit();
        }
    }

    public void handleCommandsFromClientUI(String message) throws IOException {
            String cleansedMessage = message.replaceFirst("#", "");

            String[] commandArgMessage = cleansedMessage.split(" ");
            String command = commandArgMessage[0];
            String commandArg = "";

            if (commandArgMessage.length > 1)
                commandArg = commandArgMessage[1];

            switch(command) {
                case "quit":
                    System.out.println("Quiting the system please wait...");
                    quit();
                    break;
                case "logoff":
                    this.logout();
                    break;
                case "sethost":
                    setHost(commandArg);
                    break;
                case "setport":
                    setPort(Integer.parseInt(commandArg));
                    break;
                case "login":
                    login();
                    break;
                case "gethost":
                    String host = getHost();
                    System.out.println("Connected to host: " + host);
                    break;
                case "getport":
                    int port = getPort();
                    System.out.printf("Connected on port: %d", port);
                    break;
                default:
                    System.out.println("Invalid command entered.");
                        break;
            }
    }

    /**
     * This method terminates the client.
     */
    public void quit() {
        try {
            closeConnection();
        } catch (IOException e) {
        }
        System.exit(0);
    }

}
// End of ChatClient class