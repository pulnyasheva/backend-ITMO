package org.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.common.CommandAnswer;
import org.common.EncoderUtils;

import java.io.IOException;

/**
 * The class is an encoder of {@link CommandAnswer} objects into byte messages that can be sent over a
 * communication channel. It extends {@link MessageToByteEncoder} class and overrides the encode method
 * for encoding {@link CommandAnswer} objects into byte messages.
 */
public class CommandAnswerEncoder extends MessageToByteEncoder<CommandAnswer> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CommandAnswer msg, ByteBuf out) throws Exception {
        EncoderUtils.encode(msg, out, answer -> {
            try {
                return answer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
