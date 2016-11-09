package server.core.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import server.core.codec2.Protocol;
import server.core.executor.LogicExecutor;
import server.core.executor.Task;
import server.core.log.LogUtil;
import server.core.session.ChannelSession;
import server.core.session.ChannelSessionManager;

/**
 * Created by Administrator on 2016/5/22.
 */
public class LogicHandler extends SimpleChannelInboundHandler<Protocol> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Protocol request) throws Exception {
        ChannelSession session = ChannelSessionManager.getInstance().getChannelSession(ctx.channel());
        if (session == null) return;    // 已经断开连接了，丢弃
        Task unit = new Task(request,session);
        LogicExecutor.getInstance().execute(unit);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ChannelSessionManager.getInstance().removeSession(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtil.error("",cause);
    }
}
