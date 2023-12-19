package org.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class MoveCopyFileCommand implements Command {

    private final int COUNT_ARGS = 2;

    private final String TYPE_COMMAND = "/moveAndCopyServer";

    @Override
    public ErrorInfo isCorrectCall(String[] args) {
        return CommandUtils.isCorrectCountArgs(args, COUNT_ARGS);
    }

    @Override
    public CommandRequest sendCommandToServer(String[] args) throws IOException {
        return new CommandRequest(TYPE_COMMAND, args, null);
    }

    @Override
    public CommandAnswer commandServer(CommandRequest commandRequest) throws IOException {
        String[] args = commandRequest.args();
        return copyAndMove(args[0], args[1]);
    }

    @Override
    public String clientHandler(CommandAnswer commandAnswer) {
        return commandAnswer.text();
    }

    private CommandAnswer copyAndMove(String pathFile, String pathDirectory) {
        File file = new File(pathFile);
        File directory = new File(pathDirectory);
        // Переместим и скопируем файл
        try {
            Files.createDirectories(directory.toPath());
            Files.copy(file.toPath(), directory.toPath().resolve(file.getName()));
            return new CommandAnswer(TYPE_COMMAND, "File copied successfully", null);
        } catch (IOException e) {
            return new CommandAnswer(TYPE_COMMAND, "Error copying file: " + e.getMessage(), null);
        }
    }
}
