package org.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.common.CommandAnswer;
import org.common.CommandUtils;
import org.common.CommandRequest;
import org.common.Command;

public class CommandServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * This method is called when a message is received from the client. If the message is an instance of
     * {@link CommandRequest}, it processes the command and sends a {@link CommandAnswer} back to the client.
     * The {@link CommandServerHandler} uses the {@link CommandUtils} class to retrieve the appropriate
     * Command object and execute the command. The resulting Command Answer is sent back to the client via the channel.
     *
     * @param ctx the channel handler context
     * @param msg the message received from the client
     * @throws Exception if an error occurs while handling the message
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof CommandRequest request) {
            Command command = CommandUtils.commands.get(request.typeCommand());
            CommandAnswer commandAnswer = command.commandServer(request);
            ctx.channel().writeAndFlush(commandAnswer);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
