package com.module.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.common.CommandRequest;
import org.common.EncoderUtils;

import java.io.IOException;

/**
 * The class is an encoder of {@link CommandRequest} objects into byte messages that can be sent over a
 * communication channel. It extends {@link MessageToByteEncoder} class and overrides the encode method
 * for encoding {@link CommandRequest} objects into byte messages.
 */
public class CommandRequestEncoder extends MessageToByteEncoder<CommandRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CommandRequest msg, ByteBuf out) throws Exception {
        EncoderUtils.encode(msg, out, commandRequest -> {
            try {
                return commandRequest.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
