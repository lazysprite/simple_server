package test1;

import server.core.actor.Actor;
import server.core.actor.ActorSystem;
import server.core.actor.Message;
import server.core.actor.player.Player;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ActorTest {

    public static void main(String[] args) {
        ActorSystem system = new ActorSystem();
        system.init();
        Player player = new Player(system);
        player.send(new TestMessage(), Actor.free);
    }

    static class TestMessage implements Message {
        @Override
        public void exec(Actor actor) {
            System.out.println("test");
        }
    }
}
