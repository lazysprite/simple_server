package server;

import server.core.log.LogUtil;
import server.core.net.NetBootstrap;

import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		ServerConfig.init();
		NetBootstrap net = new NetBootstrap();
		net.bootstrap();
		int wait = 3;
		while (!net.bootstrapSuccess()) {
			LogUtil.info("waiting for net bootstrap.");
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				LogUtil.error("", e);
			}
			if (wait-- == 0) {
				/** 网络启动失败 */
				System.exit(-1);
			}
		}
		LogUtil.info("net started.");
	}
	
}
