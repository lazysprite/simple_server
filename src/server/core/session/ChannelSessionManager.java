package server.core.session;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/5/21.
 */
public class ChannelSessionManager {

    private static ChannelSessionManager instance;

    private ChannelSessionManager() {}

    /**
     * 饿汉式单例
     * @return
     */
    public static ChannelSessionManager getInstance() {
        if (instance == null) {
            synchronized (ChannelSessionManager.class) {
                if (instance == null) {
                    instance = new ChannelSessionManager();
                }
            }
        }
        return instance;
    }

    private ConcurrentHashMap<Channel, ChannelSession> map = new ConcurrentHashMap<Channel, ChannelSession>();

    public ChannelSession getChannelSession(Channel ch) {
        ChannelSession session = map.get(ch);
        return session;
    }

    /**
     * 注册一个session
     * @param session
     */
    public void registerSession(ChannelSession session) {
        map.put(session.getChannel(), session);
    }

    /**
     * 移除session
     * @param channel
     */
    public void removeSession(Channel channel) {
        map.remove(channel);
    }
}
