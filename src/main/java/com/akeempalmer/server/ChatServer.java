package com.akeempalmer.server;

import com.akeempalmer.common.ChatIF;

import java.io.IOException;

public class ChatServer extends AbstractServer {

    /**
     * The interface type variable. It allows the implementation of
     * the display method in the server.
     */
    ChatIF serverUI;


    /**
     * Constructs an instance of the chat client.
     *
     * @param port     The port number to connect on.
     * @param serverUI The interface type variable.
     */
    public ChatServer( int port, ChatIF serverUI)
            throws IOException {
        super(port); // Call the superclass constructor
        this.serverUI = serverUI;
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

    }


}

