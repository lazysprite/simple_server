package test1;

import game.chat.CallBackCase;
import game.chat.ChatFilter;
import game.chat.Dialogue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/7/4.
 */
public class ChatLog implements ChatFilter {

    private Logger logger = LoggerFactory.getLogger("chat");

    @Override
    public boolean filter(Dialogue dialogue) {
        logger.info("{}|{}|{}", dialogue.getSpeakerId(), dialogue.getMessage(), dialogue.getListenerId());
        return true;
    }

    @Override
    public CallBackCase getCase() {
        return CallBackCase.NORMAL;
    }
}
