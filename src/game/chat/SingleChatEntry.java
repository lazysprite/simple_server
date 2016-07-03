package game.chat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/6/26.
 */
public class SingleChatEntry implements ChatEntry, SingleEntry {

    private final long chatId;
    private final String name;
    private final ChatCallBack callBack;
    private final Set<Long> groupSet = new HashSet<Long>();

    public SingleChatEntry(long chatId, String name, ChatCallBack callBack) {
        this.chatId = chatId;
        this.name = name;
        this.callBack = callBack;
    }

    @Override
    public boolean isGroup() {
        return false;
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
    public void speak(ChatEntry listener, Dialogue dialogue) {
        if (listener != null) {
            listener.listen(dialogue);
        } else {
            dialogue.setCallBackCase(CallBackCase.PEEROFFLINE);
            speakCallback(dialogue);
        }
    }

    @Override
    public void speakCallback(Dialogue dialogue) {
        callBack.chatCallBack(dialogue);
    }

    @Override
    public void addGroup(long group) {
        synchronized (groupSet) {
            groupSet.add(group);
        }
    }

    @Override
    public void listen(Dialogue dialogue) {
        callBack.chatCallBack(dialogue);
    }

    @Override
    public Set<Long> getGroups() {
        synchronized (groupSet) {
            return Collections.unmodifiableSet(groupSet);
        }
    }
}
