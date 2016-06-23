package test1;

import java.nio.ByteOrder;
import java.util.AbstractMap.SimpleImmutableEntry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {
	
	private int port = 8080;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void start() {
		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		NioEventLoopGroup child = new NioEventLoopGroup();
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, child);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		bootstrap.handler(new LoggingHandler(LogLevel.INFO));
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decode", new LengthFieldBasedFrameDecoder(Short.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast(new MyHandle());
			}
			
		});
	}
	
	static class MyHandle extends SimpleChannelInboundHandler<Object> {

		@Override
		protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
			
		} 
		
	}
	
	public static void main(String[] args) {
		 
	}

}
