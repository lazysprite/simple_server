package server.core.actor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/10/23.
 */
public class ActorSystem {

    private ConcurrentMap<String, Actor> actors = new ConcurrentHashMap<>();
    // TODO: 2016/10/26 线城池大小设置？
    private ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);


    public void init() {

    }

    public void submitTask(AbstractActor.ActorTask task) {
        pool.submit(task);
    }

    public Actor getOrAdd(Actor newer) {
        Actor older = actors.putIfAbsent(newer.getName(), newer);
        if (older == null) {
            return newer;
        }
        return older;
    }

    public void add(Actor actor) {
        actors.putIfAbsent(actor.getName(), actor);
    }
}
