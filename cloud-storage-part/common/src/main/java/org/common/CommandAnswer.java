package org.common;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The record represents the response received from the server after executing a command.
 * It contains information about the type of command, the text response, and any file data that was
 * returned.
 */
public record CommandAnswer(String typeCommand, String text, @Nullable FileData file) implements Serializable {

    public byte[] toByteArray() throws IOException {
        return CommandUtils.commandToByteArray(typeCommand, text, file);
    }

    public static CommandAnswer fromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        String typeCommand = (String) objectInputStream.readObject();
        String text = (String) objectInputStream.readObject();
        byte[] fileBytes = (byte[]) objectInputStream.readObject();
        FileData file = null;
        if (fileBytes != null) {
            file = FileData.fromByteArray(fileBytes);
        }

        return new CommandAnswer(typeCommand, text, file);
    }

    @Override
    public String toString() {
        return text;
    }
}
