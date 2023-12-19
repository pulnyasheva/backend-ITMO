package org.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.common.CommandRequest;
import org.common.DecoderUtils;

import java.io.IOException;
import java.util.List;

/**
 * The UserDecoder class is a decoder of byte messages received through a communication channel.
 * It extends {@link ByteToMessageDecoder} class and overrides the decode method for decoding incoming byte messages
 * to objects of the {@link CommandRequest}.
 */
public class CommandRequestDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        DecoderUtils.decode(in, out, bytes -> {
            try {
                return CommandRequest.fromByteArray(bytes);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }, CommandRequest.class);
    }
}
