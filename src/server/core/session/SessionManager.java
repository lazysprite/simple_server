package server.core.session;

import server.core.codec.Protocol;

/**
 * Created by Administrator on 2016/5/21.
 */
public class SessionManager {

    private static SessionManager instance;
    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }


    public void sendToRemote(Protocol response) {


    }
}
