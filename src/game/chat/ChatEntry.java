package game.chat;

/**
 * Created by Administrator on 2016/6/26.
 */
public interface ChatEntry {

    boolean isGroup();

    long getChatId();

    String getName();

    void speak(ChatEntry listener, Dialogue dialogue);

    void speakCallback(Dialogue dialogue);

    void listen(Dialogue dialogue);

}
