package server.core.codec;

import io.netty.buffer.ByteBuf;
import server.core.session.ChannelSession;

/**
 * Created by Administrator on 2016/5/8.
 */
public interface Protocol {
    /**
     * 请求报的逻辑处理
     * @return
     */
    Protocol executePacket(ChannelSession session);

    /**
     * 从ByteBuf中获取包的信息
     * @param msg
     */
    void readFromBuff(ByteBuf msg);

    /**
     * 将响应包的信息写入到ByteBuf中
     * @param msg
     */
    void writeToBuff(ByteBuf msg);

    /**
     * 返回json格式
     * @return
     */
    String toJsonString();
}
