package test1;

import server.core.actor.Actor;
import server.core.actor.ActorSystem;
import server.core.actor.Message;
import server.core.actor.player.Player;
import server.core.actor.service.AsyncInvocation;
import server.core.actor.service.Service;
import server.core.actor.service.ServiceActor;
import server.core.actor.service.ServiceFactory;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ServiceActorTest {

    public static void main(String[] args) {
        ActorSystem system = new ActorSystem();
        system.init();
        ServiceFactory.register("test", ServiceFactory.newService(new TestServiceActor(system)));
        Player player = new Player(system);
        player.send(new TestMessage(), Actor.free);
    }

    static class TestMessage implements Message {
        @Override
        public void exec(Actor actor) {
            System.out.println("test");
            testService().init();
            testService().helloWorld();
        }
    }

    public static class TestServiceActor extends ServiceActor implements TestService {

        @Override
        public void init() {

        }

        public TestServiceActor(ActorSystem system) {
            super(system);
        }

        @Override
        public void helloWorld() {
            System.out.println("hello world");
        }
    }

    static TestService testService() {
        return (TestService) ServiceFactory.get("test");
    }

    interface TestService extends Service {

        @AsyncInvocation
        void helloWorld();
    }
}
