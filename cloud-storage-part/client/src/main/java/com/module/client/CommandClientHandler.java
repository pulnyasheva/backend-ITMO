package com.module.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.common.CommandAnswer;
import org.common.CommandUtils;

/**
 * A client command handler that receives messages from the server and calls the corresponding callback.
 */
public class CommandClientHandler extends ChannelInboundHandlerAdapter implements ClientHandler {
    private Callback onMessageCallback;

    public CommandClientHandler(Callback onMessageCallback) {
        this.onMessageCallback = onMessageCallback;
    }


    /**
     * Method that is called when a message is received from the server.
     * Calls the onMessageCallback with the received message as a parameter.
     * And processes the corresponding command action.
     *
     * @param ctx context of the communication channel with the server.
     * @param msg received a message from the server.
     * @throws Exception if an error occurred while processing the message.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof CommandAnswer answer) {
            if (onMessageCallback != null) {
                onMessageCallback.callback(CommandUtils.commands.get(answer.typeCommand()).clientHandler(answer));
            }
        } else {
            if (onMessageCallback != null) {
                onMessageCallback.callback(msg);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    public void setOnMessageCallback(Callback newOnMessageCallback) {
        this.onMessageCallback = newOnMessageCallback;
    }
}
