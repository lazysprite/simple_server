package server.core.codec2;

import com.google.common.base.Preconditions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2016/11/6.
 */
public class PacketManager {

    private static ConcurrentMap<Integer, Class<? extends Packet>> protocols = new ConcurrentHashMap<>();

    public static Packet newPacket(int protocol) throws Exception {
        Packet packet = null;
        Class<? extends Packet> clzz = protocols.get(protocol);
        Preconditions.checkNotNull(clzz, "protocol " + protocol + "do not reister.");
        packet = clzz.newInstance();
        return packet;
    }

}
