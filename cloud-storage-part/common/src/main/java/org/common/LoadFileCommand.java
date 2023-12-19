package org.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class LoadFileCommand implements Command {

    private final int COUNT_ARGS = 2;
    private String messageAnswer;

    private final String TYPE_COMMAND = "/load";


    @Override
    public ErrorInfo isCorrectCall(String[] args) {
        ErrorInfo errorInfo = CommandUtils.isCorrectCountArgs(args, COUNT_ARGS);
        if (errorInfo.hasError()) return errorInfo;
        return CommandUtils.isHasFile(args[0]);
    }

    @Override
    public CommandRequest sendCommandToServer(String[] args) throws IOException {
        // Сохраним файл для передачи
        File file = new File(args[0]);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        long fileSize = file.length();
        String fileName = file.getName();
        FileData fileData = new FileData(fileName, fileSize, fileContent);

        return new CommandRequest("/load", args, fileData);
    }

    @Override
    public CommandAnswer commandServer(CommandRequest commandRequest) {
        FileData fileData = commandRequest.file();
        File directory = new File(commandRequest.args()[1]);
        if (!isCorrectPathDirectory(directory)) return new CommandAnswer(TYPE_COMMAND, messageAnswer, null);
        loadFile(directory, fileData);
        return new CommandAnswer(TYPE_COMMAND, messageAnswer, null);
    }

    @Override
    public String clientHandler(CommandAnswer commandAnswer) {
        return commandAnswer.text();
    }

    private boolean isCorrectPathDirectory(File directory) {
        // Проверим наличие директории
        if (directory.exists()) {
            return true;
        }
        // Создадим директории
        if (!directory.mkdirs()) {
            messageAnswer = "Could not find or create a directory on the server";
        }
        return true;
    }

    private void loadFile(File directory, FileData fileData) {
        // Проверим файл на корректность
        if (fileData.content().length != fileData.size()) {
            messageAnswer = "Failed to upload file";
            return;
        }
        File file = new File(directory, fileData.name());
        // Проверим есть ли уже файл с таким названием
        if (file.exists()) {
            messageAnswer = "A file with this name already exists";
            return;
        }
        // Загрузим файл
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(fileData.content());
            messageAnswer = "File uploaded";
        } catch (IOException e) {
            e.printStackTrace();
            messageAnswer = "Failed to upload file";
        }
    }
}
