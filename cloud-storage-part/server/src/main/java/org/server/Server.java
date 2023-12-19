package org.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    /**
     * The start method starts the server, which accepts incoming connections and processes messages from clients.
     *
     * @throws InterruptedException if an error occurred when starting the server
     */
    public void start() throws InterruptedException {
        // Создание потоков
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // инициализация объекта Bootstrap, который предоставляет настройки для сервиса
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // Установка обработчика событий для канала связи с клиентом,
                            // который будет вызывать методы MessageEncoder, UserDecoder и ServerHandler
                            // для обработки входящих и исходящих сообщений
                            pipeline.addLast(new MessageEncoder(), new UserDecoder(), new AuthorizationServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
