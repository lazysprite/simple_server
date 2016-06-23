package server.core.session;

import io.netty.channel.Channel;

/**
 * Created by Administrator on 2016/5/19.
 */
public interface ChannelSession {
    /**
     * 返回sessionid
     * @return
     */
    public long getSessionId();

    /**
     * 返回Channel
     * @return
     */
    public Channel getChannel();

}
