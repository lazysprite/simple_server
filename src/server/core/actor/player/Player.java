package server.core.actor.player;

import server.core.actor.AbstractActor;
import server.core.actor.Actor;
import server.core.actor.ActorSystem;
import server.core.actor.Message;

/**
 * Created by Administrator on 2016/10/26.
 */
public class Player extends AbstractActor {

    private long id;

    public Player(ActorSystem system) {
        super(system);
    }

    @Override
    protected void handleDeadMessage(Message message, Actor sender) {

    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public void onReceived(Message message, Actor sender) {
        message.exec(this);
    }

    public long id() {
        return id;
    }
}
