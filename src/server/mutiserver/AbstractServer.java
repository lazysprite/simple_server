package server.mutiserver;

import com.google.common.base.Preconditions;
import server.core.server.Server;
import server.mutiserver.master.Master;

/**
 * Created by Administrator on 2016/11/1.
 */
public abstract class AbstractServer implements Server {

    public static Server newServer(String type) {
        Server server = null;
        switch (type) {
            case MutiConstant.MASTER:
                server = new Master();
                break;
        }
        return Preconditions.checkNotNull(server);
    }

}
