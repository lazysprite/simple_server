package server;

import server.core.codec.Protocol;
import test.ReqHelloWorld;
import test.ResHelloWorld;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/5/8.
 */
public enum PacketType {

    ReqHelloWorld(10010001, ReqHelloWorld.class),


    ResHelloWorld(20010001, ResHelloWorld.class);


    private int protocol;
    private Class<? extends Protocol> packet;
    PacketType(int protocol, Class<? extends Protocol> clzz) {
        this.protocol = protocol;
        this.packet = clzz;
    }

    private static ConcurrentHashMap<Integer, PacketType> protocol2Type = new ConcurrentHashMap<Integer, PacketType>();
    private static ConcurrentHashMap<Class<? extends Protocol>, PacketType> class2Type = new ConcurrentHashMap<Class<? extends Protocol>, PacketType>();

    static {
        for(PacketType type : PacketType.values()) {
            protocol2Type.put(type.getProtocol(), type);
            class2Type.put(type.getPacket(), type);
        }
    }

    /**
     * 返回协议号
     * @return
     */
    public int getProtocol() {
        return this.protocol;
    }

    /**
     * 返回协议包类
     * @return
     */
    public Class<? extends Protocol> getPacket() {
        return this.packet;
    }

    /**
     * 根据协议号返回对应的协议
     * @param protocol
     * @return
     */
    public static Protocol createPacket(int protocol) {
        PacketType type = protocol2Type.get(protocol);
        if (type == null) {
            return null;
        }
        Class<? extends Protocol> clazz = type.getPacket();
        Protocol packet = null;
        try {
            packet = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            packet = null;
        }
        return packet;
    }

    /**
     * 根据协议类返回协议类型
     * @param clazz
     * @return
     */
    public static PacketType createPacketType(Class<? extends Protocol> clazz) {
        PacketType type = class2Type.get(clazz);
        return type;
    }
}
