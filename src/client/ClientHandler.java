package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import server.core.codec2.Protocol;

public class ClientHandler extends SimpleChannelInboundHandler<Protocol> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Protocol response) throws Exception {

	}
}
