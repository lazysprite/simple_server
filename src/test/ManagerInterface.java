package test;

import server.core.codec.Protocol;
import server.core.hotswap.HotSwapInterface;

/**
 * Created by Administrator on 2016/6/5.
 */
public interface ManagerInterface extends HotSwapInterface {

    Protocol handlerRequest(ReqHelloWorld reqHelloWorld);
}
