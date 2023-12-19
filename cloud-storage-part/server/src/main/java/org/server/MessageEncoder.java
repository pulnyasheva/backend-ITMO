package org.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.common.EncoderUtils;
import org.common.Message;

import java.io.IOException;

/**
 * The class is an encoder of {@link Message} objects into byte messages that can be sent over a
 * communication channel. It extends {@link MessageToByteEncoder} class and overrides the encode method
 * for encoding {@link Message} objects into byte messages.
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        EncoderUtils.encode(msg, out, message -> {
            try {
                return message.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
