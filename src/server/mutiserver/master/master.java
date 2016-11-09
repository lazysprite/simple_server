package server.mutiserver.master;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import server.core.codec2.BusinessHandler;
import server.core.codec2.GameDecoder;
import server.core.codec2.GameEncoder;
import server.mutiserver.AbstractServer;

/**
 * Created by Administrator on 2016/11/1.
 */
public class Master extends AbstractServer {


    @Override
    public ChannelInitializer<NioSocketChannel> getChannelInitializer() {
        return new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024 * 4, 1, 4, -5, 0));
                pipeline.addLast("encode", new GameEncoder());
                pipeline.addLast("decode", new GameDecoder());
                pipeline.addLast("business", new BusinessHandler());
            }
        };
    }
}
