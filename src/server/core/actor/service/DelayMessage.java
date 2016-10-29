package server.core.actor.service;

import server.core.actor.Actor;
import server.core.actor.Message;

import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2016/10/29.
 */
public class DelayMessage extends FutureTask implements Message {

    public DelayMessage(DelayCallable callable) {
        super(callable);
    }

    /** 禁用这种方式的FutureTask */
    private DelayMessage(Runnable runnable, Object result) {
        super(runnable, result);
    }

    @Override
    public void exec(Actor actor) {
        run();
    }
}
