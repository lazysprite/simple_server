package game.chat;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/27.
 */
public interface ChatService {

    void communicate(long speaker, long listener, String message);

    ChatEntry getChatEntryById(long id);

    void registerChatService(long chatId, String chatName, ChatCallBack callBack);

    void addGroup(long entry, long groupId, String groupName);

    void addGroup(long entry, Map<Long, String> groups);

    void addFilter(ChatFilter filter);
}
