package server.core.executor;

import server.core.codec.Protocol;
import server.core.session.ChannelSession;

/**
 * Created by Administrator on 2016/5/22.
 */
public interface ExecuteUnit extends Runnable{

    /**
     * 获取ChannelSession
     * @return
     */
    ChannelSession getChannelSession();

    /**
     * 获取协议对象
     * @return
     */
    Protocol getProtocol();
}
