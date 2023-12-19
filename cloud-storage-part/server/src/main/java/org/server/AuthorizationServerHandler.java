package org.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.common.Message;
import org.common.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик входящих сообщений от клиентов.
 */
public class AuthorizationServerHandler extends ChannelInboundHandlerAdapter {

    private static final Map<String, String> users = new HashMap<>();

    private String currentLogin;
    /**
     * Flag indicating completion of user authentication.
     */
    private boolean loginCompleted;
    /**
     * The number of attempts to enter an incorrect password.
     */
    private int loginAttempts = 0;


    /**
     * This method is called when a message is received from the client. If the message is an instance of User,
     * it authenticates the user and sends a message back to the client. If the authentication is successful,
     * * it removes the {@link AuthorizationServerHandler}, {@link UserDecoder}, and {@link MessageEncoder} from
     * the pipeline and adds {@link CommandAnswerEncoder}, {@link CommandRequestDecoder},
     * and {@link CommandServerHandler} to handle further communication.
     *
     * @param ctx the channel handler context
     * @param msg the message received from the client
     * @throws Exception if an error occurs while handling the message
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof User request) {
            String message = authenticateUser(request.login(), request.password());
            sendMessage(ctx.channel(), message);
            if (loginCompleted) {
                ctx.pipeline().remove(AuthorizationServerHandler.class);
                ctx.pipeline().remove(UserDecoder.class);
                ctx.pipeline().remove(MessageEncoder.class);
                ctx.pipeline().addLast(new CommandAnswerEncoder(),
                        new CommandRequestDecoder(),
                        new CommandServerHandler());
            }
        }
    }

    private String authenticateUser(String login, String password) {
        if (isEmptyLogin(login)) {
            return "Enter login";
        }

        if (isEmptyPassword(password)) {
            return "Enter password";
        }

        if (!isRegisteredUser(login)) {
            RegisteredUser(login, password);
            return login + " is registered";
        }

        // Проверяем выполнен ли вход у пользователя
        if (loginCompleted && currentLogin.equals(login)) {
            return login + " has already been completed";
        }

        updateCurrentUser(login);

        // Проверяем количество неверных попыток ввода пароля
        if (loginAttempts < 3) {
            boolean isCorrectPassword = checkPassword(login, password);
            if (isCorrectPassword) {
                loginCompleted = true;
                return login + " completed";
            } else {
                loginAttempts += 1;
                return "Invalid password";
            }
        } else {
            return "Invalid password three times";
        }
    }

    private boolean isRegisteredUser(String login) {
        return users.containsKey(login);
    }

    private void RegisteredUser(String login, String password) {
        users.put(login, password);
        loginCompleted = true;
        currentLogin = login;
    }

    private boolean checkPassword(String login, String password) {
        return users.get(login).equals(password);
    }

    private boolean isEmptyLogin(String login) {
        return login.isEmpty();
    }

    private boolean isEmptyPassword(String password) {
        return password.isEmpty();
    }

    /**
     * The method that updates the current user sets the login flag to false and resets login attempts if the user
     * has changed.
     *
     * @param login the user who tried to log in.
     */
    private void updateCurrentUser(String login) {
        if (currentLogin == null || !currentLogin.equals(login)) {
            loginAttempts = 0;
            loginCompleted = false;
            currentLogin = login;
        }
    }

    private void sendMessage(Channel channel, String message) {
        channel.writeAndFlush(new Message(message, loginCompleted));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
