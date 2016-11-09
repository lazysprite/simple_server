package server;

import server.core.log.LogUtil;
import server.core.net.NetBootstrap;
import server.core.server.Server;
import server.mutiserver.AbstractServer;

import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		ServerConfig.init();
		Server server = AbstractServer.newServer(ServerConfig.getServerType());
		NetBootstrap net = new NetBootstrap(server);
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
