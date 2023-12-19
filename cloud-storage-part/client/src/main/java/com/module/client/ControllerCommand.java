package com.module.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.common.Command;
import org.common.CommandRequest;
import org.common.CommandUtils;
import org.common.ErrorInfo;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerCommand implements Initializable {

    private final Client client = ControllerAuthorization.client;

    @FXML
    private TextField textCommand;
    @FXML
    private TextArea textAreaCommand;

    /**
     * The method send a message from the client to the server using data from the text fields
     * for login and password. To do this, a {@link CommandRequest} object is created that contains information about
     * the {@link Command}. The message is the sent via a communication channel with the server.
     */
    @FXML
    private void sendMessage() {
        String[] commandCall = textCommand.getText().trim().split("\\s+");
        textCommand.clear();
        if (isEmptyCall(commandCall) || !isCorrectCommand(commandCall[0])) {
            return;
        }
        String typeCommand = commandCall[0];
        String[] args = Arrays.copyOfRange(commandCall, 1, commandCall.length);
        Command command = CommandUtils.commands.get(typeCommand);
        if (!isCorrectCall(command, args)) return;
        CommandRequest commandRequest;
        try {
            commandRequest = command.sendCommandToServer(args);
            client.sendMessage(commandRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmptyCall(String[] args) {
        if (args.length == 0) {
            textAreaCommand.appendText("No command call\n");
            return true;
        }
        return false;
    }

    private boolean isCorrectCommand(String typeCommand) {
        if (CommandUtils.commands.containsKey(typeCommand)) {
            return true;
        }
        textAreaCommand.appendText("Invalid command\n");
        return false;
    }

    private boolean isCorrectCall(Command command, String[] args) {
        ErrorInfo error = command.isCorrectCall(args);
        if (error.hasError()) {
            textAreaCommand.appendText(error.errorMessage() + "\n");
            return false;
        }
        return true;
    }

    @FXML
    private void endSession() {
        Platform.exit();
        client.close();
    }

    /**
     * The message handler displays the received messages in the textArea text field.
     *
     * @param location  URL of the xml file location.
     * @param resources a ResourceBundle object containing application resources.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client.setOnCallbackAdapter((object) -> {
            textAreaCommand.appendText(object.toString() + "\n");
        });
    }
}
