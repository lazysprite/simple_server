package server.core.actor;

/**
 * Created by Administrator on 2016/10/15.
 */
public interface Message {

    void exec(Actor actor);

    /** 毒消息 */
    Message poison = new Message() {
        @Override
        public void exec(Actor actor) {

        }
    };

    /** 复苏消息 */
    Message recovery = new Message() {
        @Override
        public void exec(Actor actor) {

        }
    };
}
