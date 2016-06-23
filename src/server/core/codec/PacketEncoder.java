package server.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import server.ServerConfig;

/**
 * Created by Administrator on 2016/5/22.
 */
public class PacketEncoder extends MessageToByteEncoder<Protocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Protocol response, ByteBuf out) throws Exception {
        out.writeByte(ServerConfig.CodecConfig.HEAD);
        int lengthIndex = out.writerIndex();
        out.writeInt(0);
        Packet.writeProtocol(response, out);
        out.writeByte(ServerConfig.CodecConfig.TAIL);
        out.setInt(lengthIndex, out.writerIndex() - lengthIndex + 1);
    }
}
