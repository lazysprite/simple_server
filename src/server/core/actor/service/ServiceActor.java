package server.core.actor.service;

import server.core.actor.AbstractActor;
import server.core.actor.Actor;
import server.core.actor.ActorSystem;
import server.core.actor.Message;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ServiceActor extends AbstractActor {

    public ServiceActor(ActorSystem system) {
        super(system);
    }

    @Override
    protected void handleDeadMessage(Message message, Actor sender) {

    }

    @Override
    public String getName() {
        return "service";
    }

    @Override
    public void onReceived(Message message, Actor sender) {
        message.exec(this);
    }
}
