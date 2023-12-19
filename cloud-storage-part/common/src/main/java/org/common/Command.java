package org.common;

import java.io.IOException;

/**
 * The Command interface defines a standard way for sending commands to a server and receiving
 * responses from it.
 */
public interface Command {

    /**
     * Checks if the arguments passed to the command are correct and returns an ErrorInfo object with
     * information about any errors found.
     *
     * @param args the arguments passed to the command.
     * @return an {@link ErrorInfo} object representing any errors found.
     */
    ErrorInfo isCorrectCall(String[] args);

    /**
     * Sends a command request to the server with the specified arguments and returns a CommandRequest
     * object representing the request.
     *
     * @param args the arguments passed to the command.
     * @return a CommandRequest object representing the request.
     * @throws IOException if an I/O error occurs while sending the request to the server.
     */
    CommandRequest sendCommandToServer(String[] args) throws IOException;

    /**
     * Performs all necessary actions on the server
     *
     * @param commandRequest the {@link CommandRequest} object representing the request.
     * @return CommandAnswer object representing the response.
     * @throws IOException if an I/O error occurs when sending a request to the server or receiving
     *                     response from him.
     */
    CommandAnswer commandServer(CommandRequest commandRequest) throws IOException;

    /**
     * Processes the response received from the server and returns a string representing the result of
     * the command execution.
     *
     * @param commandAnswer a {@link CommandAnswer} object representing the response.
     * @return {@link String} representing the result of the command execution.
     */
    String clientHandler(CommandAnswer commandAnswer);
}
