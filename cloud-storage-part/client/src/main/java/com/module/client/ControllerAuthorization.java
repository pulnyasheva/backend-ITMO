package com.module.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.common.Message;
import org.common.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAuthorization implements Initializable {

    protected static Client client;
    @FXML
    private VBox myVBox;
    @FXML
    private TextArea textAreaAuthorization;
    @FXML
    private TextField textLogin;
    @FXML
    private TextField textPassword;

    /**
     * The method sends a message from the client to the server using data from the text fields
     * for login and password. To do this, a User object is created that contains information about the user and
     * his message. The message is then sent via a communication channel with the server.
     */
    @FXML
    private void sendMessage() {
        String login = getTextAndClearField(textLogin);
        String password = getTextAndClearField(textPassword);
        client.sendMessage(new User(login, password));
    }

    /**
     * The method that gets text from a text field, removes extra spaces and clears the text field
     */
    private String getTextAndClearField(TextField field) {
        String text = field.getText();
        field.clear();
        return text.trim();
    }

    @FXML
    private void endSession() {
        Platform.exit();
        client.close();
    }

    /**
     * The method initializes the client application by creating a Client type object and installing a message handler.
     * The message handler displays the received messages in the textAreaAuthorization text field.
     *
     * @param location  URL of the xml file location.
     * @param resources a ResourceBundle object containing application resources.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new Client((object) -> {
            textAreaAuthorization.appendText(object.toString() + "\n");
            Platform.runLater(() -> {
                // код, который изменяет пользовательский интерфейс
                if (object instanceof Message message) {
                    if (message.loginCompleted()) {
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("command.fxml"));
                        Scene scene = null;
                        try {
                            scene = new Scene(fxmlLoader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Stage stage = (Stage) myVBox.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    }
                }
            });
        });
    }
}