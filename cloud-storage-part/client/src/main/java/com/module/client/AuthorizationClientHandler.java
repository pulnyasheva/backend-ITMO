package com.module.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.common.Message;


/**
 * A client handler for authorization that receives messages from the server and calls the corresponding callback.
 */
public class AuthorizationClientHandler extends ChannelInboundHandlerAdapter implements ClientHandler {

    private Callback onMessageCallback;

    public AuthorizationClientHandler(Callback onMessageCallback) {
        this.onMessageCallback = onMessageCallback;
    }


    /**
     * Method that is called when a message is received from the server.
     * Calls the onMessageCallback with the received message as a parameter.
     * Modifies handlers after successful authorization.
     *
     * @param ctx context of the communication channel with the server.
     * @param msg received a message from the server.
     * @throws Exception if an error occurred while processing the message.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (onMessageCallback != null) {
            onMessageCallback.callback(msg);
        }
        if (msg instanceof Message message) {
            if (message.loginCompleted()) {
                ctx.pipeline().remove(UserEncoder.class);
                ctx.pipeline().remove(MessageDecoder.class);
                ctx.pipeline().remove(AuthorizationClientHandler.class);
                ClientHandler clientHandler = new CommandClientHandler(onMessageCallback);
                ctx.pipeline().addFirst(new CommandRequestEncoder(),
                        new CommandAnswerDecoder(),
                        clientHandler);
                ControllerAuthorization.client.setHandlerAdapter(clientHandler);
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
