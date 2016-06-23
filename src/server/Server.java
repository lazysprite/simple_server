package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import server.core.codec.FrameDecoder;
import server.core.codec.PacketDecoder;
import server.core.codec.PacketEncoder;
import server.core.handler.LogicHandler;
import server.core.hotswap.HotSwapMonitor;
import server.core.log.LogUtil;

public class Server {

	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
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
			new HotSwapMonitor().init();
			LogUtil.init();
			ChannelFuture f = b.bind(8080).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			
		}finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
}
