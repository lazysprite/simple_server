package server.core.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import server.ServerConfig;
import server.core.codec.FrameDecoder;
import server.core.codec.PacketDecoder;
import server.core.codec.PacketEncoder;
import server.core.handler.LogicHandler;
import server.core.log.LogUtil;

/**
 * Created by Administrator on 2016/10/29.
 */
public class NetBootstrap {

    /** 仅仅为了线程可见，不做同步处理 */
    private volatile boolean isStart = false;

    public boolean bootstrapSuccess() {
        return isStart;
    }

    public void bootstrap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                EventLoopGroup workGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_REUSEADDR, true)
//                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel sc) throws Exception {
                                    ChannelPipeline cp = sc.pipeline();
                                    cp.addLast(new FrameDecoder(12021231, 1, 4, -5, 0));
                                    cp.addLast(new PacketDecoder());
                                    cp.addLast(new PacketEncoder());
                                    cp.addLast(new LogicHandler());
                                }
                            });
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
