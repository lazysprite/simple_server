package server.core.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Administrator on 2016/11/1.
 */
public interface Server {

    ChannelInitializer<NioSocketChannel> getChannelInitializer();

    void init();

}
