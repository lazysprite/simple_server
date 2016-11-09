package server.core.executor;

import server.core.codec2.Protocol;
import server.core.session.ChannelSession;
import server.core.session.ChannelSessionManager;

/**
 * Created by Administrator on 2016/5/8.
 */
public class Task implements ExecuteUnit {
    /**
     * 执行的包
     */
    private Protocol protocol;

    private ChannelSession session;

    public Task(Protocol protocol, ChannelSession session) {
        this.protocol = protocol;
        this.session = session;
    }

    @Override
    public final void run() {
        if (protocol != null) {
            try {
                Protocol response = protocol.executePacket(getChannelSession());
                if (response == null) return;
                ChannelSession channelSession = getChannelSession();
                if (channelSession != null && channelSession.getChannel().isActive()) {
                    channelSession.getChannel().writeAndFlush(response);
                } else {
                    // 连接已经失效，丢弃响应包,断开连接并移除session
                    channelSession.getChannel().disconnect();
                    ChannelSessionManager.getInstance().removeSession(session.getChannel());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ChannelSession getChannelSession() {
        return session;
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }
}
