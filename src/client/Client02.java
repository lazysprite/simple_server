package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import server.core.codec.FrameDecoder;
import server.core.codec.PacketDecoder;
import server.core.codec.PacketEncoder;
import test.HelloStruct;
import test.ReqHelloWorld;

public class Client02 {

	public static void main(String[] args) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup());
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new FrameDecoder(12021231, 1, 4, -5, 0), new PacketDecoder(), new PacketEncoder(), new ClientHandler());
			}
			
		});
		try {
			Channel ch = bootstrap.connect("127.0.0.1", 8080).sync().channel();
			for (; ;) {
				ReqHelloWorld request = new ReqHelloWorld();
				request.setI(8);
				request.setArr(new int[]{1,2,3,4});
				request.setSarr(new String[]{"abc","efg","hij"});
				HelloStruct struct = new HelloStruct();
				struct.setName("laowang");
				request.setHs(struct);
				List<HelloStruct> list = new ArrayList<HelloStruct>();
				for (int i = 0; i < 2; i++) {
					struct.setName("laowang" + i);
					list.add(struct);
				}
				request.setList(list);
				List<int[]> alist = new ArrayList<int[]>();
				for (int i = 0; i < 2; i++) {
					alist.add(new int[]{1,2,3});
				}
				request.setIi(alist);
				request.setEnd(2);
				ch.writeAndFlush(request);
				TimeUnit.SECONDS.sleep(10);
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		} finally {

		}

	}

}
