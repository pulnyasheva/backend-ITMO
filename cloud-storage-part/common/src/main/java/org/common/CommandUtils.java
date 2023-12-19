package org.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class CommandUtils {

    public static final Map<String, Command> commands = Map.of("/load", new LoadFileCommand(),
            "/download", new DownloadFileCommand(),
            "/moveAndCopyServer", new MoveCopyFileCommand());

    public static ErrorInfo isCorrectCountArgs(String[] args, int countArgs) {
        if (args.length != countArgs) {
            String messageError = "Invalid number of arguments";
            return new ErrorInfo(true, messageError);
        }
        return new ErrorInfo(false, null);
    }

    public static ErrorInfo isHasFile(String filePath) {
        Path path = Paths.get(filePath);
        String messageError;
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                messageError = "The path to the directory is specified";
                return new ErrorInfo(true, messageError);
            } else {
                return new ErrorInfo(false, null);
            }
        } else {
            messageError = "The file does not exist";
            return new ErrorInfo(true, messageError);
        }
    }

    public static byte[] commandToByteArray(Object object1, Object object2, FileData file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(object1);
        objectOutputStream.writeObject(object2);
        if (file != null) {
            objectOutputStream.writeObject(file.toByteArray());
        } else {
            objectOutputStream.writeObject(null);
        }
        objectOutputStream.flush();

        return outputStream.toByteArray();
    }
}
