package server.core.codec2;

import io.netty.buffer.ByteBuf;

/**
 * Created by Administrator on 2016/5/8.
 */
public interface Protocol {
    /**
     * 请求报的逻辑处理
     * @return
     */
    Protocol executePacket();

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
     * 获取协议号
     * @return
     */
    int getProtocol();

}
