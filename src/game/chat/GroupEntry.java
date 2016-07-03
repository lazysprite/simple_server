package game.chat;

/**
 * Created by Administrator on 2016/6/26.
 */
public interface GroupEntry {

    void addMember(long member);

    void removeById(long chatId);
}
