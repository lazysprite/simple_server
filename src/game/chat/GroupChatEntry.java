package game.chat;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Administrator on 2016/6/26.
 */
public class GroupChatEntry implements ChatEntry, GroupEntry {

    private final ReadWriteLock lock = new ReentrantReadWriteLock(false);
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private final Set<Long> memberSet = new HashSet<Long>();
    private final long chatId;
    private final String name;
    private final ChatService service;

    GroupChatEntry(long chatId, String name, ChatService service) {
        this.chatId = chatId;
        this.name = name;
        this.service = service;
    }

    @Override
    public long getChatId() {
        return chatId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void listen(Dialogue dialogue) {
        try {
            readLock.lock();
            for (Long member : memberSet) {
                service.getChatEntryById(member).listen(dialogue);
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public void speak(ChatEntry listener, Dialogue message) {

    }

    @Override
    public void speakCallback(Dialogue dialogue) {

    }

    @Override
    public void addMember(long member) {
        try {
            writeLock.lock();
            memberSet.add(member);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeById(long chatId) {
        try {
            writeLock.lock();
            memberSet.remove(chatId);
        } finally {
            writeLock.unlock();
        }
    }
}
