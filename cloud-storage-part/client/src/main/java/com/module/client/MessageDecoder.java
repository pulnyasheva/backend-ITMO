package com.module.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.common.DecoderUtils;
import org.common.Message;

import java.io.IOException;
import java.util.List;


/**
 * The class is a decoder of byte messages received through a communication channel.
 * It extends {@link ByteToMessageDecoder} class and overrides the decode method for decoding incoming byte messages
 * to objects of the {@link Message}.
 */
public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        DecoderUtils.decode(in, out, bytes -> {
            try {
                return Message.fromByteArray(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, Message.class);
    }
}
