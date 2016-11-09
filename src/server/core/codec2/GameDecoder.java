package server.core.codec2;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import server.ServerConfig;
import server.core.log.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/11/6.
 */
public class GameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        /** buf中有多个协议数据，不能因为某一个协议解析出错，延误了其他协议解析 */
        int readIndex = in.readerIndex();
        int packetLength = 0;
        try {
            byte head = in.getByte(in.readerIndex());
            byte tail = in.getByte(in.readerIndex() + in.readableBytes() - 1);
            Preconditions.checkState(head == ServerConfig.CodecConfig.HEAD);
            Preconditions.checkState(tail == ServerConfig.CodecConfig.TAIL);
            in.skipBytes(5);
            int protocol = in.readInt();
            Packet packet = PacketManager.newPacket(protocol);
            packet.readFromBuff(in);
            out.add(packet);
        } catch (Exception e) {
            if (packetLength == 0) {
                packetLength = in.readableBytes();
            }
        } finally {
            int curIndex = in.readerIndex();
            int skip = readIndex + packetLength - curIndex;
            if (skip < 0) {
                LogUtil.error("skip negative number.");
            } else {
                in.skipBytes(packetLength);
            }
        }
    }
}
