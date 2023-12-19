package org.common;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;


public class DecoderUtils {
    @Nullable
    public static <T> void decode(ByteBuf in,
                                  List<Object> out,
                                  Function<byte[], T> fromByteArray,
                                  Class<T> clazz) {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        T obj = fromByteArray.apply(bytes);
        out.add(clazz.cast(obj));
    }
}
