package org.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

/**
 * The record representing a message that can be sent between two entities in a system.
 */
public record Message(String text, boolean loginCompleted) implements Serializable {

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeUTF(text);
        dataOutputStream.writeBoolean(loginCompleted);

        return outputStream.toByteArray();
    }

    public static Message fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        String text = dataInputStream.readUTF();
        boolean loginCompleted = dataInputStream.readBoolean();

        return new Message(text, loginCompleted);
    }

    @Override
    public String toString() {
        return text;
    }
}
