package server.core.codec2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import server.ServerConfig;
import server.core.log.LogUtil;

/**
 * Created by Administrator on 2016/11/8.
 */
public class GameEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        out.writeByte(ServerConfig.CodecConfig.HEAD);
        int writeIndex = out.writerIndex();
        out.writeInt(-1);
        out.writeInt(msg.getProtocol());
        msg.writeToBuff(out);
        out.writeByte(ServerConfig.CodecConfig.TAIL);
        int length = out.readableBytes();
        out.setInt(writeIndex, length);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtil.error("", cause);
    }
}
