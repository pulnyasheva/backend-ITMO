package org.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class DownloadFileCommand implements Command {

    private final int COUNT_ARGS = 2;

    private String[] args;

    private final String TYPE_COMMAND = "/download";

    @Override
    public ErrorInfo isCorrectCall(String[] args) {
        return CommandUtils.isCorrectCountArgs(args, COUNT_ARGS);
    }

    @Override
    public CommandRequest sendCommandToServer(String[] args) throws IOException {
        this.args = args;
        return new CommandRequest(TYPE_COMMAND, args, null);
    }

    @Override
    public CommandAnswer commandServer(CommandRequest commandRequest) throws IOException {
        ErrorInfo errorInfo = CommandUtils.isHasFile(commandRequest.args()[0]);
        if (errorInfo.hasError()) return new CommandAnswer(TYPE_COMMAND, errorInfo.errorMessage(), null);
        return downloadFile(commandRequest.args()[0]);
    }

    @Override
    public String clientHandler(CommandAnswer commandAnswer) {
        if (commandAnswer.file() != null) {
            return commandAnswer.text() + "\n" + loadFileToClient(args[1], commandAnswer.file());
        }
        return commandAnswer.text();
    }

    private CommandAnswer downloadFile(String path) throws IOException {
        File file = new File(path);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        long fileSize = file.length();
        String fileName = file.getName();
        FileData fileData = new FileData(fileName, fileSize, fileContent);
        return new CommandAnswer(TYPE_COMMAND, "The file was downloaded from the server", fileData);
    }

    private String loadFileToClient(String directory, FileData fileData) {
        // Проверим корректность полученного файла
        if (fileData.content().length != fileData.size()) {
            return "Failed to upload file";
        }
        File file = new File(directory, fileData.name());
        // Проверим, что такого файла еще нет
        if (file.exists()) {
            return "A file with this name already exists";
        }
        // Сохраним файл
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(fileData.content());
            return "File uploaded";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file";
        }
    }

}
