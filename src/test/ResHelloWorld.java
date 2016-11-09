package test;

import server.core.codec2.Packet;
import server.core.codec2.Protocol;
import server.core.session.ChannelSession;

/**
 * Created by Administrator on 2016/6/5.
 */
public class ResHelloWorld extends Packet {
    private String response;

    @Override
    public Protocol executePacket(ChannelSession session) {
        return null;
    }

    @Override
    public int getProtocol() {
        return 0;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
