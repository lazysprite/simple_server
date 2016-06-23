package server.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class PacketDecoder extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ByteBuf) {
			ByteBuf tmp = (ByteBuf) msg;
			int readerIndex = tmp.readerIndex();
			int writerIndex = tmp.writerIndex();
			tmp.setIndex(readerIndex + 5, writerIndex - 1);
			Protocol request = Packet.readProtocol(tmp);
			tmp.release();
			msg = request;
			if (msg == null) return;
		}
		super.channelRead(ctx, msg);
	}
}
