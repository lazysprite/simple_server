package game.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Administrator on 2016/6/27.
 */
public class ChatServiceImp implements ChatService {

    private final ConcurrentHashMap<Long, ChatEntry> entryMap = new ConcurrentHashMap<Long, ChatEntry>();
    private final List<ChatFilter> filters = new ArrayList<ChatFilter>();
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    @Override
    public void communicate(long speaker, long listener, String message) {
        ChatEntry speakerEntry = getChatEntryById(speaker);
        if (speakerEntry == null) return;
        ChatEntry listenerEntry = getChatEntryById(listener);
        Dialogue dialogue = new SimpleDialogue(speaker, listener, message);
        try {
            readLock.lock();
            for (int i = 0; i < filters.size(); i++) {
                ChatFilter filter = filters.get(i);
                if (!filter.filter(dialogue)) {
                    dialogue.setCallBackCase(filter.getCase());
                    break;
                }
            }
        } finally {
            readLock.unlock();
        }
        if (dialogue.getCallBackCase() == CallBackCase.NORMAL) {
            speakerEntry.speak(listenerEntry, dialogue);
        } else {
            speakerEntry.speakCallback(dialogue);
        }
    }

    @Override
    public ChatEntry getChatEntryById(long id) {
        return entryMap.get(id);
    }

    private void register(ChatEntry single) {
        entryMap.put(single.getChatId(), single);
    }

    private void registerGroup(ChatEntry group) {
        entryMap.putIfAbsent(group.getChatId(), group);
    }

    @Override
    public void registerChatService(long chatId, String chatName, ChatCallBack callBack) {
        ChatEntry newer = getChatEntryById(chatId);
        if (newer != null) {
            SingleEntry singleEntry = (SingleEntry) newer;
            Set<Long> groups = singleEntry.getGroups();
            for (Long group : groups) {
                GroupEntry groupEntry = (GroupEntry) getChatEntryById(group);
                groupEntry.removeById(newer.getChatId());
            }
        }
        newer = new SingleChatEntry(chatId, chatName, callBack);
        register(newer);
    }

    @Override
    public void addGroup(long entry, long groupId, String groupName) {
        SingleEntry single = (SingleEntry) getChatEntryById(entry);
        if (single == null) throw new ChatEntryNullPointException();
        single.addGroup(groupId);
        ChatEntry group = getChatEntryById(groupId);
        if (group == null) {
            group = new GroupChatEntry(groupId, groupName, this);
            registerGroup(group);
            group = getChatEntryById(groupId);
        }
        GroupEntry groupEntry = (GroupEntry) group;
        groupEntry.addMember(entry);
    }

    @Override
    public void addGroup(long entry, Map<Long, String> groups) {
        ChatEntry single = getChatEntryById(entry);
        if (single == null) return;
        if (groups != null) {
            for (Entry<Long, String> groupEntry : groups.entrySet()) {
                addGroup(entry, groupEntry.getKey(), groupEntry.getValue());
            }
        }
    }

    @Override
    public void addFilter(ChatFilter filter) {
        try {
            writeLock.lock();
            filters.add(filter);
        } finally {
            writeLock.unlock();
        }
    }
}
