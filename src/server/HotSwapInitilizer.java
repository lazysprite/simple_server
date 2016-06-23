package server;

import server.core.hotswap.HotSwapInterface;
import server.test.Manager;
import server.test.ManagerInterface;

/**
 * Created by Administrator on 2016/6/16.
 */
public class HotSwapInitilizer {

    public static void init() {
        HotSwapInterface.map.put(ManagerInterface.class, new Manager());
    }

}
