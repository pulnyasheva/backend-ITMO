package org.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public record FileData(String name, long size, byte[] content) implements Serializable {

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeUTF(name);
        dataOutputStream.writeLong(size);
        dataOutputStream.write(content);

        return outputStream.toByteArray();
    }

    public static FileData fromByteArray(byte[] byteArray) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        String fileName = dataInputStream.readUTF();
        long fileSize = dataInputStream.readLong();
        byte[] fileContent = new byte[(int) fileSize];
        dataInputStream.readFully(fileContent);

        return new FileData(fileName, fileSize, fileContent);
    }
}
