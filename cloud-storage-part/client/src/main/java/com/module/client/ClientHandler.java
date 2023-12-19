package com.module.client;

import io.netty.channel.ChannelHandler;

/**
 * This interface represents a client handler for a network communication channel.
 * * It extends the Channel Handler interface and adds a method to set a callback function
 * that will be called when a message is received from the client.
 */
public interface ClientHandler extends ChannelHandler {

    /**
     * Sets the callback function to be called when a message is received from the client.
     *
     * @* @param new OnMessageCallback the new callback function to be set
     */
    void setOnMessageCallback(Callback newOnMessageCallback);
}
