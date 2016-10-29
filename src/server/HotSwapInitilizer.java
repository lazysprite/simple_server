package server;

import server.core.hotswap.HotSwapInterface;
import test.Manager;
import test.ManagerInterface;

/**
 * Created by Administrator on 2016/6/16.
 */
public class HotSwapInitilizer {

    public static void init() {
        HotSwapInterface.map.put(ManagerInterface.class, new Manager());
    }

}
