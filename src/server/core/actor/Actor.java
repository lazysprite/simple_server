package server.core.actor;

import server.core.log.LogUtil;

/**
 * Created by Administrator on 2016/10/15.
 */
public interface Actor {
    String getName();

    void send(Message message, Actor sender);

    void onReceived(Message message, Actor sender);

    Actor free = new Actor() {
        @Override
        public String getName() {
            return "Actor.free";
        }

        @Override
        public void send(Message message, Actor sender) {
            LogUtil.info("actor {} send message to free actor", sender.getName());
        }

        @Override
        public void onReceived(Message message, Actor sender) {
            LogUtil.info("actor free received message {}", message);
        }
    };
}
