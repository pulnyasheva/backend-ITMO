package com.module.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.common.EncoderUtils;
import org.common.User;

import java.io.IOException;

/**
 * The class is an encoder of {@link User} objects into byte messages that can be sent over a
 * communication channel. It extends {@link MessageToByteEncoder} class and overrides the encode method
 * for encoding {@link User} objects into byte messages.
 */
public class UserEncoder extends MessageToByteEncoder<User> {

    @Override
    protected void encode(ChannelHandlerContext ctx, User msg, ByteBuf out) throws Exception {
        EncoderUtils.encode(msg, out, user -> {
            try {
                return user.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
