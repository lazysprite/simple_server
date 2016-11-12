package test;

import server.core.codec2.Packet;
import server.core.codec2.Protocol;

/**
 * Created by Administrator on 2016/6/5.
 */
public class ResHelloWorld extends Packet {
    private String response;

    @Override
    public Protocol executePacket() {
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
