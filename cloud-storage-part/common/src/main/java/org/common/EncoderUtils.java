package org.common;

import io.netty.buffer.ByteBuf;

import java.util.function.Function;

public class EncoderUtils {

    public static <T> void encode(T msg, ByteBuf out, Function<T, byte[]> toByteArray) {
        byte[] bytes = toByteArray.apply(msg);
        out.writeBytes(bytes);
    }
}
