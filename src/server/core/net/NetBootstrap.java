package server.core.net;

import com.google.common.base.Preconditions;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import server.ServerConfig;
import server.core.log.LogUtil;
import server.core.server.Server;

/**
 * Created by Administrator on 2016/10/29.
 */
public class NetBootstrap {

    /** 仅仅为了线程可见，不做同步处理 */
    private volatile boolean isStart = false;
    private Server server;

    public NetBootstrap(Server server) {
        this.server = server;
    }

    public boolean bootstrapSuccess() {
        return isStart;
    }

    public void bootstrap() {
        Preconditions.checkNotNull(server, "server field can not be null.");
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup(ServerConfig.bossGroup());
                EventLoopGroup workGroup = new NioEventLoopGroup(ServerConfig.workerGroup());
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_REUSEADDR, true)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(server.getChannelInitializer());
                    ChannelFuture f = b.bind(ServerConfig.getServerPort()).sync();
                    isStart = true;
                    f.channel().closeFuture().sync();
                } catch (Exception e) {
                    LogUtil.error("net bootstrap failed.", e);
                }finally {
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                }
            }
        }).start();
    }

}
