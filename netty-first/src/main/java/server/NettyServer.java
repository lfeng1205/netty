package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * 服务端
 *
 * @author qian
 */
public class NettyServer {

    private static final int BEGIN_PORT = 8000;

    public static void main(String[] args) {
        //监听端口
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //处理每一条连接的数据读写的线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //引导类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        final AttributeKey<Object> clientKey = AttributeKey.newInstance("clientKey");

        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInboundHandlerAdapter() {
                    protected void initChannel(ChannelHandlerContext ctx) throws Exception {
                        super.channelActive(ctx);
                    }
                }).attr(AttributeKey.newInstance("serverName"), "nettyServer")
                .childAttr(clientKey, "clientValue")
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        System.out.println(ch.attr(clientKey).get());
                    }
                });

        //绑定端口
        bind(serverBootstrap, BEGIN_PORT);
    }

    /**
     * 绑定端口
     * @param serverBootstrap
     * @param port
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
