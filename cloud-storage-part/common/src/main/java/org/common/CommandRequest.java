package org.common;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The record represents a request to be sent to the server to execute a command. It
 * contains information about the type of command, the arguments passed to it, and any file data to
 * be sent along with the request.
 */
public record CommandRequest(String typeCommand, String[] args, @Nullable FileData file) implements Serializable {

    public byte[] toByteArray() throws IOException {
        return CommandUtils.commandToByteArray(typeCommand, args, file);
    }

    public static CommandRequest fromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        String typeCommand = (String) objectInputStream.readObject();
        String[] args = (String[]) objectInputStream.readObject();
        byte[] fileBytes = (byte[]) objectInputStream.readObject();
        FileData file = null;
        if (fileBytes != null) {
            file = FileData.fromByteArray(fileBytes);
        }

        return new CommandRequest(typeCommand, args, file);
    }
}
