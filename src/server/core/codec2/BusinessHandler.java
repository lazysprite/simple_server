package server.core.codec2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Administrator on 2016/11/8.
 */
public class BusinessHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet msg) throws Exception {

    }
}
