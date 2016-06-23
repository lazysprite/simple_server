package server.core.session;

import io.netty.channel.Channel;
import server.core.session.ChannelSession;

/**
 * Created by Administrator on 2016/5/21.
 */
public class ChannelSessionImp implements ChannelSession {

    private long sessionId;

    private Channel ch;

    public ChannelSessionImp(Channel channel, long sessionId) {
        this.ch = channel;
        this.sessionId = sessionId;
    }

    @Override
    public long getSessionId() {
        return sessionId;
    }

    @Override
    public Channel getChannel() {
        return this.ch;
    }
}
