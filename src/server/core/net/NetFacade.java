package server.core.net;

/**
 * 网络层的使用接口,需要线程安全
 * Created by Administrator on 2016/11/12.
 */
public class NetFacade {
    /** 单例 */
    private volatile static NetFacade instance;

    public static NetFacade getInstance() {
        if (instance == null) {
            synchronized (NetFacade.class) {
                if (instance == null) {
                    instance = new NetFacade();
                }
            }
        }
        return instance;
    }


}
