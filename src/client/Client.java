package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Random;

public class Client {

	public static void main(String[] args) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup());
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
		});
		try {
			Channel ch = bootstrap.connect("127.0.0.1", 8080).sync().channel();
			System.out.println("connect ...");
			ByteBufAllocator allocator = ch.alloc();
			ByteBuf buff = allocator.buffer();
			String test = "hello world.";
			buff.writeByte(-81);
			buff.writeInt(test.getBytes().length + 6);
			buff.writeBytes(test.getBytes());
			buff.writeByte(-82);
			int longger = 0;
			int shorter = 0;
			for (; ;) {
				ByteBuf copy = buff.copy();
				int random = new Random().nextInt(10);
				if (1 < random && random < 3) {
					copy.setInt(copy.readerIndex() + 1, test.getBytes().length + 6 + 2);
					longger++;
				} else if (4 < random && random < 6) {
					copy.setInt(copy.readerIndex() + 1, test.getBytes().length + 6 - 2);
					shorter++;
				}
				ch.writeAndFlush(copy);
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		} finally {

		}

	}
	
}
