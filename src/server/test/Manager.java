package server.test;

import server.core.codec.Packet;
import server.core.hotswap.HotSwapInterface;

/**
 * Created by Administrator on 2016/6/5.
 */
public class Manager implements ManagerInterface {

    @Override
    public String getHotSwapName() {
        return "Manager";
    }

    @Override
    public Packet handlerRequest(ReqHelloWorld reqHelloWorld) {
        ResHelloWorld response = new ResHelloWorld();
        response.setResponse("hello");
        return response;
    }
}
