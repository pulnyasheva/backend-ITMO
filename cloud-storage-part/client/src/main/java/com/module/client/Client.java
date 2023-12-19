package com.module.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.SocketChannel;

import static org.common.Constants.HOST;
import static org.common.Constants.PORT;

public class Client {
    private SocketChannel channel;

    private ClientHandler handlerAdapter;

    /**
     * Constructor of the Client class, which creates a new instance of the client to connect to the server.
     *
     * @param onMessageCallback a {@link Callback} object that will be called when new messages are received from
     *                          the server.
     */
    public Client(Callback onMessageCallback) {
        Thread t = new Thread(() -> {
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                // Инициализация объекта Bootstrap, который предоставляет настройки для клиента
                Bootstrap b = new Bootstrap();
                handlerAdapter = new AuthorizationClientHandler(onMessageCallback);
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                // Установка обработчика событий для канала связи с сервером,
                                // который будет вызывать методы UserEncoder, MessageDecoder и ClientHandler
                                // для обработки входящих и исходящих сообщений
                                socketChannel.pipeline().addLast(new UserEncoder(), new MessageDecoder(), handlerAdapter);
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.start();
    }

    public void close() {
        channel.close();
    }

    public void setHandlerAdapter(ClientHandler clientHandler) {
        this.handlerAdapter = clientHandler;
    }

    public void setOnCallbackAdapter(Callback newCallback) {
        this.handlerAdapter.setOnMessageCallback(newCallback);
    }

    /**
     * Sends a message from the client to the server.
     *
     * @param object a {@link Object} type object containing information about the user.
     */
    public void sendMessage(Object object) {
        channel.writeAndFlush(object);
    }
}
