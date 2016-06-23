package server.test;

import server.core.codec.ProtocolStruct;

/**
 * Created by Administrator on 2016/5/24.
 */
public class HelloStruct extends ProtocolStruct {
    private String name = "xiaoming";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
