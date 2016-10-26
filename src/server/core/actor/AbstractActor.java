package server.core.actor;

import server.core.log.LogUtil;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 问题1：send 的时候需不需要执行最后的停止检查
 * 问题2：run 中发送的复苏消息如果发生队列满的情况，如何处理
 * Created by Administrator on 2016/10/15.
 */
public abstract class AbstractActor implements Actor {
    /** 状态常量 */
    private final static int ACTOR_IDLE = 0;
    private final static int ACTOR_RUNNING = 1;
    private final static int ACTOR_DEAD = 2;
    private final static int DEADHANDLE_STOP = 0;
    private final static int DEADHANDLE_RUNNING = 1;

    private final AtomicInteger state = new AtomicInteger(ACTOR_IDLE);
    private final static int MAX_MESSAGE = 64;
    private final static int MAX_ONCE_SUBMIT = 64;
    private final BlockingQueue<Mail> mailBox = new ArrayBlockingQueue<Mail>(MAX_MESSAGE);

    private final AtomicInteger deadHandleState = new AtomicInteger(DEADHANDLE_STOP);

    private final ActorTask task = new ActorTask();
    private ActorSystem system;

    public AbstractActor(ActorSystem system) {
        Objects.requireNonNull(system);
        this.system = system;
        system.add(this);
    }

    /**
     * 在send方法中，actor 可能存在两种状态：idle 和 dead
     * 这里假设 actor 在 running 状态下入队的消息都会处理
     * @param message
     * @param sender
     */
    @Override
    public final void send(Message message, Actor sender) {
        if (state.get() == ACTOR_DEAD) {
            throw new RuntimeException("actor already stop.");
        }

        sender = sender == null ? Actor.free : sender;
        Mail mail = new Mail(sender, message);
        if (mailBox.offer(mail)) {
            /**
             * idle -> idle
             * dead -> dead 开头已经处理
             * running -> idle
             * running -> dead
             **/
            if (state.compareAndSet(ACTOR_IDLE, ACTOR_RUNNING)) {
                /** 处理：idle -> idle, running -> idle
                 * idle -> running 的状态转变，提交到线程池执行
                 */
                system.submitTask(task);
            }
            if (state.get() == ACTOR_DEAD) {
                /** 处理：running -> dead
                 *  actor 已经停止，试图启动死消息处理
                 **/
                handleDeadMessage();
            }
        } else {
            if (state.compareAndSet(ACTOR_IDLE, ACTOR_RUNNING)) {
                system.submitTask(task);
            }
            // FIXME: 2016/10/26 需要启动死消息处理吗？
            throw new MailBoxFullException("actor message full: " + getName());
        }

    }

    abstract protected void handleDeadMessage(Message message, Actor sender);

    private void handleDeadMessage() {
        if (deadHandleState.compareAndSet(DEADHANDLE_STOP, DEADHANDLE_RUNNING)) {
            Mail mail;
            while ((mail = mailBox.peek()) != null) {
                try {
                    handleDeadMessage(mail.message, mail.sender);
                } catch (Throwable e) {
                    LogUtil.error("handle dead message error",e);
                }
                mailBox.poll();
            }
        }
    }


    class ActorTask implements Runnable {

        /**
         * 在run方法中的actor的状态只可能是running状态
         * 有可能天成idle或者dead状态，并且
         * running --> dead 状态切换是不可翻转的
         */
        @Override
        public void run() {
            int maxTask = MAX_ONCE_SUBMIT;
            Mail mail;

            while ((mail = mailBox.peek()) != null) {
                if (mail.message == Message.recovery) {
                    /** 复苏消息，跳过 */
                    mailBox.poll();
                    continue;
                }

                if (mail.message == Message.poison) {
                    /** 毒消息 */
                    state.compareAndSet(ACTOR_RUNNING, ACTOR_DEAD);
                    mailBox.poll();
                    handleDeadMessage();
                    return;
                }

                try {
                    onReceived(mail.message, mail.sender);
                } catch (Throwable e) {
                    LogUtil.error("actor {} error while handling message.", getName());
                }

                mailBox.poll();

                if (--maxTask <= 0) {
                    /** 防止独占线程太久，主动放弃 */
                    break;
                }
            }

            /** 处理完一批消息，设置为空闲状态 */
            if (state.compareAndSet(ACTOR_RUNNING, ACTOR_IDLE)) {
                /** 设置为空闲状态成功 */
                if (mailBox.peek() != null) {
                    /** 队列中还有消息，发送复苏消息 */
                    try {
                        send(Message.recovery, null);
                    } catch (MailBoxFullException e) {
                        /** 发送复活消息的是队满，忽视该异常 */
                        LogUtil.error("ignore mailbox full exception while sending recovery message");
                    }
                }
            }
        }
    }

    private class Mail {
        private Actor sender;
        private Message message;

        Mail(Actor sender, Message message) {
            this.sender = sender;
            this.message = message;
        }
    }
}
