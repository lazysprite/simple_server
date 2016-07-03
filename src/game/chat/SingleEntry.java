package game.chat;

import java.util.Set;

/**
 * Created by Administrator on 2016/7/3.
 */
public interface SingleEntry {

    Set<Long> getGroups();

    void addGroup(long group);
}
