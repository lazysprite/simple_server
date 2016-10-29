package test1;

import server.core.codec.Packet;
import server.core.codec.Protocol;
import test.HelloStruct;

public class Test {
	private static long Length = 1000000000L;
	public static void main(String[] args) {
		System.out.println("int.class:" + int.class.isPrimitive());
		System.out.println("HelloStruct:" + Protocol.class.isInstance(new HelloStruct()));
		System.out.println("HelloStruct:" + HelloStruct.class.isAssignableFrom(Packet.class));
		System.out.println("HelloStruct:" + Protocol.class.isAssignableFrom(HelloStruct.class));
	}
	
}
